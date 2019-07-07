package com.lmartino.bank.bdd.steps;

import com.google.gson.Gson;
import com.lmartino.bank.app.Application;
import com.lmartino.bank.rest.dto.AccountDto;
import com.lmartino.bank.rest.dto.CreateAccountDto;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.math.BigDecimal;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class AccountSteps {
    private String accountName;
    private BigDecimal balance;
    private AccountDto responseBody;

    @Before
    public void setUp() throws SQLException {
        // Starting main jvm process
        Application.start();
    }

    @After
    public void tearDown() {
        Application.stop();
    }

    @Given("^Account name (.*)$")
    public void accountName(String accountName) {
        this.accountName = accountName;
    }

    @Given("^Money balance (.*)$")
    public void balance(BigDecimal balance) {
        this.balance = balance;
    }

    @When("^user create new account$")
    public void user_create_new_account() throws Throwable {
        if (accountName==null||balance==null) throw new IllegalArgumentException("Missing required input data");
        CreateAccountDto createAccountDto = new CreateAccountDto(accountName, balance);
        final String payload = new Gson().toJson(createAccountDto);
        responseBody = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("/api/accounts")
                .then()
                .statusCode(201)
                .extract()
                .as(AccountDto.class);
    }

    @Then("^account is created$")
    public void account_is_created() throws Throwable {
        Assert.assertThat(responseBody, is(notNullValue()));
    }

    @Then("^account id is available$")
    public void account_id_is_available() throws Throwable {
        Assert.assertThat(responseBody.getId(), is(notNullValue()));
    }

    @Then("^balance is (.*)$")
    public void balance_is(BigDecimal balance) throws Throwable {
        Assert.assertThat(responseBody.getBalance(), is(balance));
    }

    @Then("^account name is (.*)$")
    public void account_name_is(String accountName) throws Throwable {
        Assert.assertThat(responseBody.getName(), is(accountName));
    }

}
