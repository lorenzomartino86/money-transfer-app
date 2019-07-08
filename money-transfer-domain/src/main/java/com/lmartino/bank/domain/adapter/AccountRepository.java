package com.lmartino.bank.domain.adapter;

import com.lmartino.bank.domain.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Account saveAccount(final Account account);

    Account updateAccount(final Account account);

    Optional<Account> getAccountBy(final String accountId);

    List<Account> getAllAccounts();

}
