package com.lmartino.bank.rest;

import com.google.gson.Gson;
import com.lmartino.bank.domain.exception.AccountNameConflictException;
import com.lmartino.bank.domain.exception.UnknownAccountException;
import com.lmartino.bank.domain.exception.UnknownCurrencyCodeException;
import com.lmartino.bank.domain.model.*;
import com.lmartino.bank.domain.usecase.CreateAccountUseCase;
import com.lmartino.bank.domain.usecase.GetAccountTransfersUseCase;
import com.lmartino.bank.domain.usecase.GetAccountUseCase;
import com.lmartino.bank.rest.dto.AccountDto;
import com.lmartino.bank.rest.dto.AccountTransferDto;
import com.lmartino.bank.rest.dto.CreateAccountDto;
import io.restassured.http.ContentType;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import spark.Spark;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static spark.Spark.*;

public class AccountRestApiTest {
    private CreateAccountUseCase mockCreateAccountUseCase = createNiceMock(CreateAccountUseCase.class);
    private GetAccountUseCase mockGetAccountUseCase = createNiceMock(GetAccountUseCase.class);
    private GetAccountTransfersUseCase mockGetAccountTransfersUseCase = createNiceMock(GetAccountTransfersUseCase.class);
    private AccountRestApi api = new AccountRestApi(mockCreateAccountUseCase, mockGetAccountUseCase, mockGetAccountTransfersUseCase);


    @Before
    public void setUp() throws Exception {
        Spark.stop();
        port(8080);
        api.init();
        awaitInitialization();
    }

    @After
    public void tearDown() throws Exception {
        Spark.stop();
        awaitStop();
    }

    @Test
    public void canCreateAccount() {
        CreateAccountDto createAccountDto = new CreateAccountDto("Foo", BigDecimal.valueOf(10.0), "EUR");

        Account newAccount = Account.createNewAccount(createAccountDto.getName(),
                Money.of(createAccountDto.getBalance(), Currency.of(createAccountDto.getCurrency())));
        expect(mockCreateAccountUseCase.compose(EasyMock.anyString(), EasyMock.anyObject(BigDecimal.class), EasyMock.anyString()))
                .andReturn(newAccount).times(1);
        replay(mockCreateAccountUseCase);

        final String payload = new Gson().toJson(createAccountDto);
        AccountDto accountDto = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("/api/accounts")
                .then()
                .statusCode(201)
                .extract()
                .as(AccountDto.class);

        Assert.assertThat(accountDto.getId(), is(notNullValue()));
        Assert.assertThat(accountDto.getName(), is(createAccountDto.getName()));
        Assert.assertThat(accountDto.getCurrency(), is(createAccountDto.getCurrency()));
        Assert.assertThat(accountDto.getBalance().doubleValue(), is(createAccountDto.getBalance().doubleValue()));
        Assert.assertThat(accountDto.getCreatedAt(), is(notNullValue()));

    }


    @Test
    public void cannotCreateAccountWithConflictingNames() {
        CreateAccountDto createAccountDto = new CreateAccountDto("Foo", BigDecimal.valueOf(10.0), "EUR");
        expect(mockCreateAccountUseCase.compose(EasyMock.anyString(), EasyMock.anyObject(BigDecimal.class), EasyMock.anyString()))
                .andThrow(new AccountNameConflictException("")).times(1);
        replay(mockCreateAccountUseCase);

        final String payload = new Gson().toJson(createAccountDto);
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("/api/accounts")
                .then()
                .statusCode(409);
    }


    @Test
    public void cannotCreateAccountWithUnknownCurrencyCode() {
        CreateAccountDto createAccountDto = new CreateAccountDto("Foo", BigDecimal.valueOf(10.0), "EUR");
        expect(mockCreateAccountUseCase.compose(EasyMock.anyString(), EasyMock.anyObject(BigDecimal.class), EasyMock.anyString()))
                .andThrow(new UnknownCurrencyCodeException("")).times(1);
        replay(mockCreateAccountUseCase);

        final String payload = new Gson().toJson(createAccountDto);
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("/api/accounts")
                .then()
                .statusCode(400);
    }

    @Test
    public void canGetAccount() {
        CreateAccountDto createAccountDto = new CreateAccountDto("Foo", BigDecimal.valueOf(10.0), "EUR");
        Account mockAccount = Account.createNewAccount(createAccountDto.getName(),
                Money.of(createAccountDto.getBalance(), Currency.of(createAccountDto.getCurrency())));
        expect(mockGetAccountUseCase.compose(EasyMock.anyString()))
                .andReturn(mockAccount).times(1);
        replay(mockGetAccountUseCase);

        AccountDto accountDto = given()
                .contentType(ContentType.JSON)
                .get("/api/accounts/123")
                .then()
                .statusCode(200)
                .extract()
                .as(AccountDto.class);

        Assert.assertThat(accountDto.getId(), is(notNullValue()));
        Assert.assertThat(accountDto.getName(), is(createAccountDto.getName()));
        Assert.assertThat(accountDto.getCurrency(), is(createAccountDto.getCurrency()));
        Assert.assertThat(accountDto.getBalance().doubleValue(), is(createAccountDto.getBalance().doubleValue()));
        Assert.assertThat(accountDto.getCreatedAt(), is(notNullValue()));

    }


    @Test
    public void canGetAccountTransfers() {
        AccountTransfer mockAccountTransfer = AccountTransfer.of(Id.of("123"), TransferType.DEPOSIT,
                Money.of(BigDecimal.ONE, Currency.of("EUR")), "description", LocalDateTime.now());
        expect(mockGetAccountTransfersUseCase.compose(EasyMock.anyString()))
                .andReturn(asList(mockAccountTransfer)).times(1);
        replay(mockGetAccountTransfersUseCase);

        AccountTransferDto[] accountTransferDtos = given()
                .contentType(ContentType.JSON)
                .get("/api/accounts/123/transfers")
                .then()
                .statusCode(200)
                .extract()
                .as(AccountTransferDto[].class);

        Assert.assertThat(accountTransferDtos, is(notNullValue()));
        Assert.assertThat(accountTransferDtos.length, is(1));
        Assert.assertThat(accountTransferDtos[0].getId(), is(mockAccountTransfer.getId().getValue()));
        Assert.assertThat(accountTransferDtos[0].getAmount(), is(mockAccountTransfer.getMoney().getValue()));
        Assert.assertThat(accountTransferDtos[0].getType(), is(mockAccountTransfer.getType().name()));
        Assert.assertThat(accountTransferDtos[0].getCurrency(), is(mockAccountTransfer.getMoney().getCurrency().getValue()));

    }

    @Test
    public void cannotGetMissingAccount() {
        expect(mockGetAccountUseCase.compose(EasyMock.anyString()))
                .andThrow(new UnknownAccountException("")).times(1);
        replay(mockGetAccountUseCase);

        given()
                .contentType(ContentType.JSON)
                .get("/api/accounts/123")
                .then()
                .statusCode(404);

    }

    @Test
    public void canGetAllAccounts() {
        CreateAccountDto createAccountDto = new CreateAccountDto("Foo", BigDecimal.valueOf(10.0), "EUR");

        Account mockAccount = Account.createNewAccount(createAccountDto.getName(),
                Money.of(createAccountDto.getBalance(), Currency.of(createAccountDto.getCurrency())));
        expect(mockGetAccountUseCase.compose())
                .andReturn(Arrays.asList(mockAccount)).times(1);
        replay(mockGetAccountUseCase);

        final String payload = new Gson().toJson(createAccountDto);
        AccountDto[] accountDtos = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .get("/api/accounts")
                .then()
                .statusCode(200)
                .extract()
                .as(AccountDto[].class);

        Assert.assertThat(accountDtos, is(notNullValue()));
        Assert.assertThat(accountDtos.length, is(1));
        AccountDto accountDto = accountDtos[0];
        Assert.assertThat(accountDto.getId(), is(notNullValue()));
        Assert.assertThat(accountDto.getName(), is(createAccountDto.getName()));
        Assert.assertThat(accountDto.getCurrency(), is(createAccountDto.getCurrency()));
        Assert.assertThat(accountDto.getBalance().doubleValue(), is(createAccountDto.getBalance().doubleValue()));
        Assert.assertThat(accountDto.getCreatedAt(), is(notNullValue()));

    }

}