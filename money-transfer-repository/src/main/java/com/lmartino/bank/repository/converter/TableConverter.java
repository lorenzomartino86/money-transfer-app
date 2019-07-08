package com.lmartino.bank.repository.converter;

import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.Amount;
import com.lmartino.bank.domain.model.Currency;
import com.lmartino.bank.domain.model.Id;
import com.lmartino.bank.repository.entity.AccountTable;

import static com.lmartino.bank.repository.converter.DateTimeConverter.toDate;
import static com.lmartino.bank.repository.converter.DateTimeConverter.toLocalDateTime;

public final class TableConverter {

    public static AccountTable toAccountTable(Account account) {
        AccountTable accountTable = new AccountTable();
        accountTable.setId(account.getId().getValue());
        accountTable.setName(account.getName());
        accountTable.setBalance(account.getBalance().getMoney());
        accountTable.setCreatedBy(toDate(account.getCreatedAt()));
        accountTable.setCurrency(account.getCurrency().getValue());
        return accountTable;
    }

    public static Account toDomainModel(AccountTable accountTable) {
        return Account.of(Id.of(accountTable.getId()),
                accountTable.getName(),
                Amount.of(accountTable.getBalance()),
                toLocalDateTime(accountTable.getCreatedBy()),
                Currency.of(accountTable.getCurrency())
        );
    }

}
