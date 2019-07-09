package com.lmartino.bank.app.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.lmartino.bank.domain.adapter.AccountRepository;
import com.lmartino.bank.domain.adapter.TransferRepository;
import com.lmartino.bank.repository.AccountDAO;
import com.lmartino.bank.repository.TransferDAO;

import java.sql.SQLException;

public class RepositoryModule extends AbstractModule {
    @Override
    protected void configure() {}

    @Provides @Singleton @Named("jdbcPooledConnectionSource")
    JdbcPooledConnectionSource jdbcPooledConnectionSource() throws SQLException {
        JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource("jdbc:h2:mem:bank;DB_CLOSE_DELAY=-1");
        return connectionSource;
    }

    @Provides @Singleton @Named("accountRepository")
    AccountRepository accountRepository(@Named("jdbcPooledConnectionSource") final JdbcPooledConnectionSource datasource)
            throws SQLException {
        return new AccountDAO(datasource);
    }


    @Provides @Singleton @Named("transferRepository")
    TransferRepository transferRepository(@Named("jdbcPooledConnectionSource") final JdbcPooledConnectionSource datasource,
                                          @Named("accountRepository") final AccountRepository accountRepository
                                          ) throws SQLException {
        return new TransferDAO(accountRepository, datasource);
    }


}
