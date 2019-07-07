package com.lmartino.bank.repository.configuration;

import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

import java.sql.SQLException;

public class JdbcConnectionFactory {

    @Provides
    public JdbcPooledConnectionSource jdbcConnectionFactory(@Named(value = "database.url") final String databaseUrl) throws SQLException {
        return new JdbcPooledConnectionSource(databaseUrl);
    }
}
