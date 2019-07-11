package com.lmartino.bank.repository;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.Currency;
import com.lmartino.bank.domain.model.Money;
import com.lmartino.bank.repository.entity.AccountTable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class AccountDAOTest {
    private Currency eur = Currency.of("EUR");
    private AccountDAO accountDAO;

    @Before
    public void setUp() throws SQLException {
        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource("jdbc:h2:mem:bank;DB_CLOSE_DELAY=-1");
        TableUtils.createTableIfNotExists(connectionSource, AccountTable.class);
        accountDAO = new AccountDAO(connectionSource);
    }

    @Test
    public void canCreateNewAccount(){
        Account requestedAccount = Account.createNewAccount("Test Account",
                Money.of(BigDecimal.valueOf(1000.99), eur));
        Account createdAccount = accountDAO.saveAccount(requestedAccount);
        Assert.assertThat(createdAccount, is(notNullValue()));
        Assert.assertThat(createdAccount, is(requestedAccount));
    }

    @Test
    public void canGetAccountById(){
        Account requestedAccount = Account.createNewAccount("Test Account",
                Money.of(BigDecimal.valueOf(1000.99), eur));
        accountDAO.saveAccount(requestedAccount);
        Optional<Account> account = accountDAO.getAccountBy(requestedAccount.getId().getValue());
        Assert.assertThat(account.isEmpty(), is(false));
        Assert.assertThat(account.get(), is(requestedAccount));
    }

}