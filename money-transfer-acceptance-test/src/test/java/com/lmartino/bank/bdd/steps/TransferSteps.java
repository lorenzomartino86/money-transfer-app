package com.lmartino.bank.bdd.steps;

import com.lmartino.bank.app.Application;
import com.lmartino.bank.rest.dto.TransferDto;
import cucumber.api.java.After;
import cucumber.api.java.Before;

import java.math.BigDecimal;
import java.sql.SQLException;

public class TransferSteps {
    private String fromAccountId;
    private String toAccountId;
    private BigDecimal amount;
    private TransferDto responseBody;

    @Before
    public void setUp() throws SQLException {
        // Starting main jvm process
        // Application.main(null);
    }

    @After
    public void tearDown() {
        // Terminates current running jvm process as 0 status code (it's not a failure)
        // System.exit(0);
    }


}
