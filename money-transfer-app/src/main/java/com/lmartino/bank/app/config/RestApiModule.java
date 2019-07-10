package com.lmartino.bank.app.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.lmartino.bank.domain.usecase.CreateAccountUseCase;
import com.lmartino.bank.domain.usecase.GetAccountTransfersUseCase;
import com.lmartino.bank.domain.usecase.GetAccountUseCase;
import com.lmartino.bank.domain.usecase.MakeTransferUseCase;
import com.lmartino.bank.rest.AccountRestApi;
import com.lmartino.bank.rest.BankTransferRestApi;

public class RestApiModule extends AbstractModule {
    @Override
    protected void configure() { }

    @Provides @Singleton
    AccountRestApi createAccountUseCase(
            @Named("createAccountUseCase") final CreateAccountUseCase createAccountUseCase,
            @Named("getAccountUseCase") final GetAccountUseCase getAccountUseCase,
            @Named("getAccountTransfersUseCase") final GetAccountTransfersUseCase getAccountTransfersUseCase
    ){
        return new AccountRestApi(createAccountUseCase, getAccountUseCase, getAccountTransfersUseCase);
    }

    @Provides @Singleton
    BankTransferRestApi bankTransferRestApi(
            @Named("makeTransferUseCase") final MakeTransferUseCase makeTransferUseCase
    ){
        return new BankTransferRestApi(makeTransferUseCase);
    }
}
