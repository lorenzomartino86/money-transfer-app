package com.lmartino.bank.app.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.adapter.TransferRepository;
import com.lmartino.bank.domain.model.AllowedCurrencies;
import com.lmartino.bank.domain.usecase.CreateAccountUseCase;
import com.lmartino.bank.domain.usecase.GetAccountUseCase;
import com.lmartino.bank.domain.usecase.MakeTransferUseCase;

import java.util.Arrays;

public class DomainModule extends AbstractModule {
    @Override
    protected void configure() { }

    @Provides @Singleton @Named("allowedCurrencies")
    AllowedCurrencies allowedCurrencies(){
        return new AllowedCurrencies(Arrays.asList("EUR", "USD", "GBP"));
    }

    @Provides @Singleton @Named("createAccountUseCase")
    CreateAccountUseCase createAccountUseCase(@Named("accountRepository") final AccountRepository accountRepository,
                                              @Named("allowedCurrencies") final AllowedCurrencies allowedCurrencies){
        return new CreateAccountUseCase(accountRepository, allowedCurrencies);
    }

    @Provides @Singleton @Named("getAccountUseCase")
    GetAccountUseCase getAccountUseCase(@Named("accountRepository") final AccountRepository accountRepository){
        return new GetAccountUseCase(accountRepository);
    }

    @Provides @Singleton @Named("makeTransferUseCase")
    MakeTransferUseCase makeTransferUseCase(@Named("accountRepository") final AccountRepository accountRepository,
                                            @Named("transferRepository") final TransferRepository transferRepository){
        return new MakeTransferUseCase(accountRepository, transferRepository);
    }

}
