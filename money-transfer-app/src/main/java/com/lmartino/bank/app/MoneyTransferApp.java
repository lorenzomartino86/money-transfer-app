package com.lmartino.bank.app;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lmartino.bank.app.config.AppModule;
import com.lmartino.bank.app.config.DomainModule;
import com.lmartino.bank.app.config.RepositoryModule;
import com.lmartino.bank.app.config.RestApiModule;
import com.lmartino.bank.app.exception.MoneyTransferAppInitException;
import com.lmartino.bank.domain.model.ExchangeRate;
import com.lmartino.bank.repository.entity.AccountTable;
import com.lmartino.bank.repository.entity.ExchangeRateTable;
import com.lmartino.bank.repository.entity.TransferTable;
import com.lmartino.bank.rest.AccountRestApi;
import com.lmartino.bank.rest.BankTransferRestApi;
import spark.Spark;

import java.sql.SQLException;

import static spark.Spark.*;


public class MoneyTransferApp{

    private final AccountRestApi accountRestApi;
    private final BankTransferRestApi bankTransferRestApi;
    private final JdbcPooledConnectionSource datasource;

    @Inject
    MoneyTransferApp(final AccountRestApi accountRestApi,
                     final BankTransferRestApi bankTransferRestApi,
                     final @Named("jdbcPooledConnectionSource") JdbcPooledConnectionSource datasource
                     ){
        this.accountRestApi = accountRestApi;
        this.bankTransferRestApi = bankTransferRestApi;
        this.datasource = datasource;
    }

    public void start() {
        stop();
        // Configure Spark
        port(8080);
        int maxThreads = 8;
        int minThreads = 2;
        int timeOutMillis = 30000;
        threadPool(maxThreads, minThreads, timeOutMillis);

        Spark.init();

        accountRestApi.init();
        bankTransferRestApi.init();

        // Initialize database with injected datasource
        try {
            TableUtils.createTableIfNotExists(datasource, AccountTable.class);
            TableUtils.createTableIfNotExists(datasource, TransferTable.class);
            TableUtils.createTableIfNotExists(datasource, ExchangeRateTable.class);
        } catch (SQLException e) {
            throw new MoneyTransferAppInitException(e);
        }

        awaitInitialization(); // Wait for server to be initialized

    }

    public void stop(){
        Spark.stop();
        awaitStop();
    }

    public static MoneyTransferApp init() {
        Injector instance = Guice.createInjector(
                new RepositoryModule(),
                new DomainModule(),
                new RestApiModule(),
                new AppModule()
        );
        return instance.getInstance(MoneyTransferApp.class);
    }

    public static void main(final String... args) throws SQLException {
        MoneyTransferApp app = init();
        app.start();
    }

}
