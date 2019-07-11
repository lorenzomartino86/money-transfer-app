package com.lmartino.bank.domain.usecase;

import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.model.Account;

import java.util.List;
import java.util.Optional;

import static com.lmartino.bank.domain.exception.DomainExceptionHandler.unkownAccountException;

public class GetAccountUseCase {
    private AccountRepository accountRepository;

    public GetAccountUseCase(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account compose(final String accountId) {
        Optional<Account> optionalAccount = accountRepository.getAccountBy(accountId);
        if (optionalAccount.isEmpty()) unkownAccountException(accountId);
        return optionalAccount.get();
    }

    public List<Account> compose() {
        return accountRepository.getAllAccounts();
    }

}
