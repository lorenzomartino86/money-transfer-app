package com.lmartino.bank.domain.usecase;

import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.Amount;

public class CreateAccountUseCase {
    private AccountRepository accountRepository;

    public CreateAccountUseCase(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account compose(final String name, final double balance) {
        Account account = Account.createNewAccount(name, Amount.of(balance));
        return accountRepository.saveAccount(account);
    }
}
