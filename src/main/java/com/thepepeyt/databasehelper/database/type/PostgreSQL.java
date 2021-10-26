package com.thepepeyt.databasehelper.database.type;

import com.thepepeyt.databasehelper.database.AbstractSQLDatabase;

import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQL extends AbstractSQLDatabase {
    public PostgreSQL(final String database, final String host, final String user,
            final String password, final int port) {
        super(database, host, user, password, port);
    }

    public void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + database, user, password);
    }
}
