package com.lmartino.bank.domain.usecase;

import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.Amount;

import java.math.BigDecimal;

public class CreateAccountUseCase {
    private AccountRepository accountRepository;

    public CreateAccountUseCase(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account compose(final String name, final BigDecimal balance, final String currency) {
        Account account = Account.createNewAccount(name, Amount.of(balance), null);
        return accountRepository.saveAccount(account);
    }
}
