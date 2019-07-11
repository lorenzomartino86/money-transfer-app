package com.lmartino.bank.repository;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lmartino.bank.domain.exception.UnprocessableTransferException;
import com.lmartino.bank.domain.model.*;
import com.lmartino.bank.repository.entity.AccountTable;
import com.lmartino.bank.repository.entity.TransferTable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TransferDAOTest {
    private Currency eur = Currency.of("EUR");
    private Currency usd = Currency.of("USD");
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
        Account foo = accountDAO.saveAccount(Account.createNewAccount("Foo", Money.of(initialFooBalance, eur)));
        Account bar = accountDAO.saveAccount(Account.createNewAccount("Bar", Money.of(initialBarBalance, eur)));

        Transfer requestedTransfer = Transfer.makeTransfer(foo, bar, Money.of(transferAmount, eur), "Robbing from rich and giving to the poor", BigDecimal.ONE);
        Transfer createdTransfer = transferDAO.saveTransfer(requestedTransfer);
        Assert.assertThat(createdTransfer, is(notNullValue()));
        Assert.assertThat(createdTransfer, is(requestedTransfer));

        Optional<Account> updatedFoo = accountDAO.getAccountBy(foo.getId().getValue());
        Assert.assertThat(updatedFoo.get().getBalance(), is(Money.of(initialFooBalance.subtract(transferAmount), eur)));

        Optional<Account> updatedBar = accountDAO.getAccountBy(bar.getId().getValue());
        Assert.assertThat(updatedBar.get().getBalance(), is(Money.of(initialBarBalance.add(transferAmount), eur)));

    }


    @Test
    public void accountBalanceIsRestoredIfMakeTransferTransactionFails() throws SQLException {
        BigDecimal initialFooBalance = BigDecimal.valueOf(1250);
        BigDecimal initialBarBalance = BigDecimal.valueOf(321.99);
        BigDecimal transferAmount = BigDecimal.valueOf(450.50);
        Account foo = accountDAO.saveAccount(Account.createNewAccount("Foo", Money.of(initialFooBalance, eur)));
        Account bar = accountDAO.saveAccount(Account.createNewAccount("Bar", Money.of(initialBarBalance, eur)));

        Transfer requestedTransfer = Transfer.makeTransfer(foo, bar, Money.of(transferAmount, eur), "Robbing from rich and giving to the poor", BigDecimal.ONE);

        try{
            // Dropping transfer table at runtime to simulate a failure during the save transfer transaction
            TableUtils.dropTable(connectionSource, TransferTable.class, false);
            transferDAO.saveTransfer(requestedTransfer);
            Assert.fail();
        } catch(UnprocessableTransferException e){
            // Checking that rollback has restored initial balance in account foo and bar

            Optional<Account> updatedFoo = accountDAO.getAccountBy(foo.getId().getValue());
            Assert.assertThat(updatedFoo.get().getBalance(), is(Money.of(initialFooBalance, eur)));

            Optional<Account> updatedBar = accountDAO.getAccountBy(bar.getId().getValue());
            Assert.assertThat(updatedBar.get().getBalance(), is(Money.of(initialBarBalance, eur)));
        }

    }


    @Test
    public void canGetTransfersByAccountId(){
        BigDecimal initialFooBalance = BigDecimal.valueOf(1250);
        BigDecimal initialBarBalance = BigDecimal.valueOf(321.99);
        BigDecimal transferAmount = BigDecimal.valueOf(450.50);
        String fooName = UUID.randomUUID().toString();
        String barName = UUID.randomUUID().toString();
        Account foo = accountDAO.saveAccount(Account.createNewAccount(fooName, Money.of(initialFooBalance, eur)));
        Account bar = accountDAO.saveAccount(Account.createNewAccount(barName, Money.of(initialBarBalance, usd)));

        Transfer eurToUsd = Transfer.makeTransfer(foo, bar, Money.of(transferAmount, eur),
                "Robbing from rich and giving to the poor", BigDecimal.valueOf(1.12));
        Transfer usdToEur = Transfer.makeTransfer(bar, foo, Money.of(transferAmount, usd),
                "Robbing from poor and giving to the rich", BigDecimal.valueOf(0.98));
        Transfer eurToUsdTransfer = transferDAO.saveTransfer(eurToUsd);
        Transfer usdToEurTransfer = transferDAO.saveTransfer(usdToEur);

        List<AccountTransfer> transfersByFoo = transferDAO.getTransfersBy(foo.getId().getValue());

        Assert.assertThat(transfersByFoo.size(), is(2));

        // Check first transaction is a WITHDRAW
        Assert.assertThat(transfersByFoo.get(0).getId(), is(eurToUsdTransfer.getId()));
        Assert.assertThat(transfersByFoo.get(0).getType(), is(TransferType.WITHDRAW));
        Assert.assertThat(transfersByFoo.get(0).getMoney(), is(eurToUsdTransfer.getWithdrawAmount()));
        Assert.assertThat(transfersByFoo.get(0).getDescription(), is(eurToUsdTransfer.getDescription()));
        Assert.assertThat(transfersByFoo.get(0).getCreatedAt(), is(eurToUsdTransfer.getCreatedAt()));

        // Check second transaction is a DEPOSIT
        Assert.assertThat(transfersByFoo.get(1).getId(), is(usdToEurTransfer.getId()));
        Assert.assertThat(transfersByFoo.get(1).getType(), is(TransferType.DEPOSIT));
        Assert.assertThat(transfersByFoo.get(1).getMoney(), is(usdToEurTransfer.getDepositAmount()));
        Assert.assertThat(transfersByFoo.get(1).getDescription(), is(usdToEurTransfer.getDescription()));
        Assert.assertThat(transfersByFoo.get(1).getCreatedAt(), is(usdToEurTransfer.getCreatedAt()));

    }

}