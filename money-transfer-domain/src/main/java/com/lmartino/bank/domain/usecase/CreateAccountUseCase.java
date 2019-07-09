package com.lmartino.bank.domain.usecase;

import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.AllowedCurrencies;
import com.lmartino.bank.domain.model.Amount;
import com.lmartino.bank.domain.model.Currency;

import java.math.BigDecimal;

public class CreateAccountUseCase {
    private AccountRepository accountRepository;
    private AllowedCurrencies allowedCurrencies;

    public CreateAccountUseCase(final AccountRepository accountRepository, final AllowedCurrencies allowedCurrencies) {
        this.accountRepository = accountRepository;
        this.allowedCurrencies = allowedCurrencies;
    }

    public Account compose(final String name, final BigDecimal balance, final String currency) {
        allowedCurrencies.isAllowed(currency);
        Account account = Account.createNewAccount(name, Amount.of(balance), Currency.of(currency));
        return accountRepository.saveAccount(account);
    }
}
