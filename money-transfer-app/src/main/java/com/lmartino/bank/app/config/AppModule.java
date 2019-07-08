package com.lmartino.bank.app.config;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.lmartino.bank.app.MoneyTransferApp;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MoneyTransferApp.class).in(Singleton.class);
    }
}
