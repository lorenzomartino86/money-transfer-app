package com.lmartino.bank.domain.usecase;

import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.Currency;
import com.lmartino.bank.domain.model.Money;

import java.math.BigDecimal;
import java.util.Optional;

import static com.lmartino.bank.domain.exception.DomainExceptionHandler.accountNameConflictException;

public class CreateAccountUseCase {
    private AccountRepository accountRepository;

    public CreateAccountUseCase(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account compose(final String name, final BigDecimal balance, final String currency) {
        Optional<Account> accountCheck = accountRepository.getAccountByName(name);
        if (accountCheck.isPresent()) accountNameConflictException(name);

        Account account = Account.createNewAccount(name, Money.of(balance, Currency.of(currency)));
        return accountRepository.saveAccount(account);
    }
}
