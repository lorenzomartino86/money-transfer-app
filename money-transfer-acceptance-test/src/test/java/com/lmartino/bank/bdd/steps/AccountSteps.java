package com.lmartino.bank.bdd.steps;

import com.lmartino.bank.app.Application;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.sql.SQLException;

public class AccountSteps {

    @Before
    public void setUp() throws SQLException {
        Application.main(null);
    }

    @Given("^Account name (.*)$")
    public void accountName(String accountName) {
    }

    @Given("^Money balance (.*)$")
    public void balance(Double balance) {
    }


    @When("^user create new account$")
    public void user_create_new_account() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^account is created$")
    public void account_is_created() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^account id is available$")
    public void account_id_is_available() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

}
