package com.lmartino.bank.repository;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.repository.converter.TableConverter;
import com.lmartino.bank.repository.entity.AccountTable;
import com.lmartino.bank.repository.exception.RepositoryException;
import lombok.extern.java.Log;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.lmartino.bank.repository.converter.TableConverter.toAccountTable;
import static com.lmartino.bank.repository.converter.TableConverter.toDomainModel;

@Log
public class AccountDAO extends BaseDaoImpl<AccountTable, String> implements AccountRepository {

    public AccountDAO(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, AccountTable.class);
    }

    @Override
    public Account saveAccount(Account account) {
        try {
            saveAccountTable(account);
            return account;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Account updateAccount(Account account) {
        try {
            updateAccountTable(account);
            return account;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Account> getAccountBy(String accountId){
        try {
            List<AccountTable> accountTableList = super.queryForEq("id", accountId);
            if (accountTableList.isEmpty()){
                return Optional.empty();
            }
            AccountTable accountTable = accountTableList.get(0);
            return Optional.of(toDomainModel(accountTable));
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        try {
            List<AccountTable> accountTableList = super.queryForAll();
            return accountTableList.stream()
                    .map(TableConverter::toDomainModel)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Account> getAccountByName(String accountName) {
        try {
            List<AccountTable> accountTableList = super.queryForEq("name", accountName);
            if (accountTableList.isEmpty()){
                return Optional.empty();
            } else {
                return Optional.of(toDomainModel(accountTableList.get(0)));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    protected AccountTable saveAccountTable(Account account) throws SQLException {
        AccountTable accountTable = toAccountTable(account);
        super.create(accountTable);
        return accountTable;
    }

    protected AccountTable updateAccountTable(Account account) throws SQLException {
        AccountTable accountTable = toAccountTable(account);
        super.update(accountTable);
        return accountTable;
    }

}
