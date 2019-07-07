package com.lmartino.bank.app;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lmartino.bank.domain.adapter.TransferRepository;
import com.lmartino.bank.domain.usecase.CreateAccountUseCase;
import com.lmartino.bank.domain.usecase.GetAccountUseCase;
import com.lmartino.bank.domain.usecase.MakeTransferUseCase;
import com.lmartino.bank.repository.AccountDAO;
import com.lmartino.bank.repository.TransferDAO;
import com.lmartino.bank.repository.entity.AccountTable;
import com.lmartino.bank.repository.entity.TransferTable;
import com.lmartino.bank.rest.AccountRestApi;
import com.lmartino.bank.rest.BankTransferRestApi;
import spark.Spark;

import java.sql.SQLException;

import static spark.Spark.*;

public class Application {

    public static void main(String[] args) throws SQLException {
        start();
    }

    public static void start() throws SQLException {
        stop();
        // Configure Spark
        port(8080);
        int maxThreads = 8;
        int minThreads = 2;
        int timeOutMillis = 30000;
        threadPool(maxThreads, minThreads, timeOutMillis);
        init();

        awaitInitialization(); // Wait for server to be initialized

        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource("jdbc:h2:mem:bank;DB_CLOSE_DELAY=-1");
        TableUtils.createTableIfNotExists(connectionSource, AccountTable.class);
        TableUtils.createTableIfNotExists(connectionSource, TransferTable.class);

        // Repository Configuration
        AccountDAO accountRepository = new AccountDAO(connectionSource);
        TransferRepository transferRepository = new TransferDAO(accountRepository, connectionSource);

        // Use case configuration
        CreateAccountUseCase createAccountUseCase = new CreateAccountUseCase(accountRepository);
        GetAccountUseCase getAccountUseCase = new GetAccountUseCase(accountRepository);
        MakeTransferUseCase makeTransferUseCase = new MakeTransferUseCase(accountRepository, transferRepository);

        AccountRestApi.init(createAccountUseCase, getAccountUseCase);
        BankTransferRestApi.init(makeTransferUseCase);
    }

    public static void stop(){
        Spark.stop();
        awaitStop();
    }

}
