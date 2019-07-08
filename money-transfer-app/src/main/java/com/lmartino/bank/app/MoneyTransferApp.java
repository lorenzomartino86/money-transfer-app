package com.lmartino.bank.app;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.lmartino.bank.app.config.AppModule;
import com.lmartino.bank.app.config.DomainModule;
import com.lmartino.bank.app.config.RepositoryModule;
import com.lmartino.bank.app.config.RestApiModule;
import com.lmartino.bank.rest.AccountRestApi;
import com.lmartino.bank.rest.BankTransferRestApi;
import spark.Spark;

import java.sql.SQLException;

import static spark.Spark.*;


public class MoneyTransferApp{

    private final AccountRestApi accountRestApi;
    private final BankTransferRestApi bankTransferRestApi;

    @Inject
    MoneyTransferApp(final AccountRestApi accountRestApi, final BankTransferRestApi bankTransferRestApi){
        this.accountRestApi = accountRestApi;
        this.bankTransferRestApi = bankTransferRestApi;
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
