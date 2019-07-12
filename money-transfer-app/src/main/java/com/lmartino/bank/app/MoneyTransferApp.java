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
import com.lmartino.bank.domain.adapter.ExchangeRateRepository;
import com.lmartino.bank.domain.model.Currency;
import com.lmartino.bank.domain.model.ExchangeRate;
import com.lmartino.bank.repository.entity.AccountTable;
import com.lmartino.bank.repository.entity.ExchangeRateTable;
import com.lmartino.bank.repository.entity.TransferTable;
import com.lmartino.bank.rest.AccountRestApi;
import com.lmartino.bank.rest.BankTransferRestApi;
import spark.Spark;

import java.math.BigDecimal;
import java.sql.SQLException;

import static spark.Spark.*;


public class MoneyTransferApp{
    private final AccountRestApi accountRestApi;
    private final BankTransferRestApi bankTransferRestApi;
    private final JdbcPooledConnectionSource datasource;
    private final ExchangeRateRepository exchangeRateRepository;

    @Inject
    MoneyTransferApp(final AccountRestApi accountRestApi,
                     final BankTransferRestApi bankTransferRestApi,
                     final @Named("jdbcPooledConnectionSource") JdbcPooledConnectionSource datasource,
                     final @Named("exchangeRateRepository") ExchangeRateRepository exchangeRateRepository){
        this.accountRestApi = accountRestApi;
        this.bankTransferRestApi = bankTransferRestApi;
        this.datasource = datasource;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public void start() {
        stop();
        // Configure Spark
        int port = 8080;
        if (System.getenv("PORT") != null){
            port = Integer.parseInt(System.getenv("PORT"));
        }
        port(port);
        int maxThreads = 8;
        int minThreads = 2;
        int timeOutMillis = 30000;
        threadPool(maxThreads, minThreads, timeOutMillis);

        Spark.init();

        accountRestApi.init();
        bankTransferRestApi.init();

        initDatabase();

        awaitInitialization(); // Wait for server to be initialized

    }

    private void initDatabase() {
        try {
            // Initialize tables with injected datasource
            TableUtils.createTableIfNotExists(datasource, AccountTable.class);
            TableUtils.createTableIfNotExists(datasource, TransferTable.class);
            TableUtils.createTableIfNotExists(datasource, ExchangeRateTable.class);
            // Initialize exchange rates repository
            ExchangeRate eurusd = ExchangeRate.create(Currency.of("EUR"), Currency.of("USD"), BigDecimal.valueOf(1.12077));
            ExchangeRate usdeur = ExchangeRate.create(Currency.of("USD"), Currency.of("EUR"), BigDecimal.valueOf(0.89224));
            ExchangeRate gbpeur = ExchangeRate.create(Currency.of("GBP"), Currency.of("EUR"), BigDecimal.valueOf(1.11210));
            ExchangeRate eurgbp = ExchangeRate.create(Currency.of("EUR"), Currency.of("GBP"), BigDecimal.valueOf(0.89919));
            ExchangeRate gbpusd = ExchangeRate.create(Currency.of("GBP"), Currency.of("USD"), BigDecimal.valueOf(1.25));
            ExchangeRate usdgbp = ExchangeRate.create(Currency.of("USD"), Currency.of("GBP"), BigDecimal.valueOf(0.80));
            exchangeRateRepository.saveRates(eurusd, usdeur, gbpeur, eurgbp, gbpusd, usdgbp);
        } catch (SQLException e) {
            throw new MoneyTransferAppInitException(e);
        }
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

    public static void main(final String... args) {
        MoneyTransferApp app = init();
        app.start();
    }

}
