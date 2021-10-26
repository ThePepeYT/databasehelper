package com.thepepeyt.databasehelper.database.type;

import com.thepepeyt.databasehelper.database.AbstractSQLDatabase;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL extends AbstractSQLDatabase {
    public MySQL(final String database, final String host, final String user,
            final String password, final int port) {
        super(database, host, user, password, port);
    }

    @Override
    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
    }
}












