package com.lmartino.bank.rest;

import com.google.gson.Gson;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.Currency;
import com.lmartino.bank.domain.model.Money;
import com.lmartino.bank.domain.model.Transfer;
import com.lmartino.bank.domain.usecase.MakeTransferUseCase;
import com.lmartino.bank.rest.dto.MakeTransferDto;
import com.lmartino.bank.rest.dto.TransferDto;
import io.restassured.http.ContentType;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import spark.Spark;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static spark.Spark.*;

public class BankTransferRestApiTest {
    private MakeTransferUseCase mockMakeTransferUseCase = createNiceMock(MakeTransferUseCase.class);
    private BankTransferRestApi api = new BankTransferRestApi(mockMakeTransferUseCase);


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
    public void canMakeTransfer() {
        MakeTransferDto dto = new MakeTransferDto("id1","id2",
                BigDecimal.valueOf(12.99),"EUR","notes");

        Account foo = Account.createNewAccount("foo", Money.of(BigDecimal.TEN, Currency.of("EUR")));
        Account bar = Account.createNewAccount("bar", Money.of(BigDecimal.TEN, Currency.of("EUR")));
        Transfer mockTransfer = Transfer.makeTransfer(foo, bar, Money.of(BigDecimal.ONE, Currency.of("EUR")), "notes", BigDecimal.ONE);
        expect(mockMakeTransferUseCase.compose(EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject(Money.class), EasyMock.anyString()))
                .andReturn(mockTransfer).times(1);
        replay(mockMakeTransferUseCase);

        final String payload = new Gson().toJson(dto);
        TransferDto transferDto = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("/api/transfers")
                .then()
                .statusCode(201)
                .extract()
                .as(TransferDto.class);

        Assert.assertThat(transferDto.getId(), is(notNullValue()));
        Assert.assertThat(transferDto.getCreatedAt(), is(notNullValue()));
        Assert.assertThat(transferDto.getFromAccountId(), is(foo.getId().getValue()));
        Assert.assertThat(transferDto.getToAccountId(), is(bar.getId().getValue()));
        Assert.assertThat(transferDto.getDescription(), is(mockTransfer.getDescription()));
        Assert.assertThat(transferDto.getCurrency(), is(mockTransfer.getWithdrawAmount().getCurrency().getValue()));
        Assert.assertThat(transferDto.getAmount(), is(mockTransfer.getWithdrawAmount().getValue()));

    }

}