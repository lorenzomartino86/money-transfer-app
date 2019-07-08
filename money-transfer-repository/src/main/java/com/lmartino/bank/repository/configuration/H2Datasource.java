package com.lmartino.bank.repository.configuration;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

import java.sql.SQLException;

public class H2Datasource extends JdbcPooledConnectionSource{

    public H2Datasource() throws SQLException {
        super("jdbc:h2:mem:bank;DB_CLOSE_DELAY=-1");
    }
}
