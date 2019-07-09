package com.lmartino.bank.repository;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lmartino.bank.domain.exception.UnprocessableTransferException;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.Amount;
import com.lmartino.bank.domain.model.Currency;
import com.lmartino.bank.domain.model.Transfer;
import com.lmartino.bank.repository.entity.AccountTable;
import com.lmartino.bank.repository.entity.TransferTable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TransferDAOTest {
    private AccountDAO accountDAO;
    private TransferDAO transferDAO;
    private JdbcPooledConnectionSource connectionSource;

    @Before
    public void setUp() throws SQLException {
        connectionSource = new JdbcPooledConnectionSource("jdbc:h2:mem:bank;DB_CLOSE_DELAY=-1");
        TableUtils.createTableIfNotExists(connectionSource, AccountTable.class);
        TableUtils.createTableIfNotExists(connectionSource, TransferTable.class);
        accountDAO = new AccountDAO(connectionSource);
        transferDAO = new TransferDAO(accountDAO, connectionSource);
    }

    @Test
    public void accountsBalanceIsUpdatedWhenMakeTransferTransactionIsCommitted(){
        BigDecimal initialFooBalance = BigDecimal.valueOf(1250);
        BigDecimal initialBarBalance = BigDecimal.valueOf(321.99);
        BigDecimal transferAmount = BigDecimal.valueOf(450.50);
        Account foo = accountDAO.saveAccount(Account.createNewAccount("Foo", Amount.of(initialFooBalance), Currency.of("EUR")));
        Account bar = accountDAO.saveAccount(Account.createNewAccount("Bar", Amount.of(initialBarBalance), Currency.of("EUR")));

        Transfer requestedTransfer = Transfer.makeTransfer(foo, bar, Amount.of(transferAmount), "Robbing from rich and giving to the poor", null);
        Transfer createdTransfer = transferDAO.saveTransfer(requestedTransfer);
        Assert.assertThat(createdTransfer, is(notNullValue()));
        Assert.assertThat(createdTransfer, is(requestedTransfer));

        Optional<Account> updatedFoo = accountDAO.getAccountBy(foo.getId().getValue());
        Assert.assertThat(updatedFoo.get().getBalance(), is(Amount.of(initialFooBalance.subtract(transferAmount))));

        Optional<Account> updatedBar = accountDAO.getAccountBy(bar.getId().getValue());
        Assert.assertThat(updatedBar.get().getBalance(), is(Amount.of(initialBarBalance.add(transferAmount))));

    }


    @Test
    public void accountBalanceIsRestoredIfMakeTransferTransactionFails() throws SQLException {
        BigDecimal initialFooBalance = BigDecimal.valueOf(1250);
        BigDecimal initialBarBalance = BigDecimal.valueOf(321.99);
        BigDecimal transferAmount = BigDecimal.valueOf(450.50);
        Account foo = accountDAO.saveAccount(Account.createNewAccount("Foo", Amount.of(initialFooBalance), Currency.of("EUR")));
        Account bar = accountDAO.saveAccount(Account.createNewAccount("Bar", Amount.of(initialBarBalance), Currency.of("EUR")));

        Transfer requestedTransfer = Transfer.makeTransfer(foo, bar, Amount.of(transferAmount), "Robbing from rich and giving to the poor", null);

        try{
            // Dropping transfer table at runtime to simulate a failure during the save transfer transaction
            TableUtils.dropTable(connectionSource, TransferTable.class, false);
            transferDAO.saveTransfer(requestedTransfer);
            Assert.fail();
        } catch(UnprocessableTransferException e){
            // Checking that rollback has restored initial balance in account foo and bar

            Optional<Account> updatedFoo = accountDAO.getAccountBy(foo.getId().getValue());
            Assert.assertThat(updatedFoo.get().getBalance(), is(Amount.of(initialFooBalance)));

            Optional<Account> updatedBar = accountDAO.getAccountBy(bar.getId().getValue());
            Assert.assertThat(updatedBar.get().getBalance(), is(Amount.of(initialBarBalance)));
        }

    }

}