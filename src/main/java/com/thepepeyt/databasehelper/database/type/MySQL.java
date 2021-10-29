package com.thepepeyt.databasehelper.database.type;

import com.thepepeyt.databasehelper.database.AbstractSQLDatabase;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL extends AbstractSQLDatabase {
    public MySQL(final String database, final String host, final String user,
            final String password, final int port) {
        super(database, host, user, password, port);
    }

    @Override
    public void connect() throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setAutoCommit(true);

        connection = new HikariDataSource(config).getConnection();




    }
}












