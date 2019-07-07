package com.lmartino.bank.bdd.steps;

import com.google.gson.Gson;
import com.lmartino.bank.app.Application;
import com.lmartino.bank.rest.dto.AccountDto;
import com.lmartino.bank.rest.dto.CreateAccountDto;
import com.lmartino.bank.rest.dto.MakeTransferDto;
import com.lmartino.bank.rest.dto.TransferDto;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;

import java.math.BigDecimal;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class TransferBetweenTwoAccountsSteps {
    private AccountDto fromAccount;
    private AccountDto toAccount;
    private TransferDto responseBody;

    @Before
    public void setUp() throws SQLException {
        Application.start();
    }

    @After
    public void tearDown() {
        Application.stop();
    }

    @Given("^account (.*) with balance (.*)$")
    public void account_with_balance(String accountName, BigDecimal balance) throws Throwable {
        CreateAccountDto createAccountDto = new CreateAccountDto(accountName, balance);
        final String payload = new Gson().toJson(createAccountDto);
        if (fromAccount == null){
            fromAccount = given()
                    .contentType(ContentType.JSON)
                    .body(payload)
                    .post("/api/accounts")
                    .then()
                    .statusCode(201)
                    .extract()
                    .as(AccountDto.class);
        } else {
            toAccount = given()
                    .contentType(ContentType.JSON)
                    .body(payload)
                    .post("/api/accounts")
                    .then()
                    .statusCode(201)
                    .extract()
                    .as(AccountDto.class);
        }

    }

    @When("^user makes transfer of (.*) with note (.*)$")
    public void user_makes_transfer_between_accounts_with_note(BigDecimal amount, String description) throws Throwable {
        MakeTransferDto makeTransferDto = new MakeTransferDto(fromAccount.getId(), toAccount.getId(), amount, description);
        final String payload = new Gson().toJson(makeTransferDto);
        responseBody = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("/api/transfers")
                .then()
                .statusCode(201)
                .extract()
                .as(TransferDto.class);
    }

    @Then("^transfer is completed$")
    public void transfer_is_completed() throws Throwable {
        assertThat(responseBody, is(notNullValue()));
        assertThat(responseBody.getId(), is(notNullValue()));
    }

    @Then("^source account has balance (.*) reduced by (.*)$")
    public void source_account_has_balance_reduced(BigDecimal originalBalance, BigDecimal reduction) throws Throwable {
        AccountDto updatedSourceAccount = given()
                .get("/api/accounts/" + fromAccount.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(AccountDto.class);
        assertThat(updatedSourceAccount.getBalance().doubleValue(), is(originalBalance.subtract(reduction).doubleValue()));
    }

    @Then("^target account has balance (.*) increased by (.*)$")
    public void target_account_has_balance_increased(BigDecimal originalBalance, BigDecimal increase) throws Throwable {
        AccountDto updatedTargetAccount = given()
                .get("/api/accounts/" + toAccount.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(AccountDto.class);
        assertThat(updatedTargetAccount.getBalance().doubleValue(), is(originalBalance.add(increase).doubleValue()));
    }


}
