package com.lmartino.bank.rest;

import com.google.gson.Gson;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.Currency;
import com.lmartino.bank.domain.model.Money;
import com.lmartino.bank.domain.usecase.CreateAccountUseCase;
import com.lmartino.bank.domain.usecase.GetAccountTransfersUseCase;
import com.lmartino.bank.domain.usecase.GetAccountUseCase;
import com.lmartino.bank.rest.dto.AccountDto;
import com.lmartino.bank.rest.dto.CreateAccountDto;
import io.restassured.http.ContentType;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import spark.Spark;

import java.math.BigDecimal;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
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
    public void canGetAccount() {
        CreateAccountDto createAccountDto = new CreateAccountDto("Foo", BigDecimal.valueOf(10.0), "EUR");

        Account mockAccount = Account.createNewAccount(createAccountDto.getName(),
                Money.of(createAccountDto.getBalance(), Currency.of(createAccountDto.getCurrency())));
        expect(mockGetAccountUseCase.compose(EasyMock.anyString()))
                .andReturn(mockAccount).times(1);
        replay(mockGetAccountUseCase);

        final String payload = new Gson().toJson(createAccountDto);
        AccountDto accountDto = given()
                .contentType(ContentType.JSON)
                .body(payload)
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