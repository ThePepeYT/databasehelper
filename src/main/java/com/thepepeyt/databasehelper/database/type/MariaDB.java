package com.thepepeyt.databasehelper.database.type;

import com.thepepeyt.databasehelper.database.AbstractSQLDatabase;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDB extends AbstractSQLDatabase {
    public MariaDB(final String database, final String host, final String user,
                      final String password, final int port) {
        super(database, host, user, password, port);
    }

    public void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mariadb://" + host + ":" + port + "/" + database, user, password);
    }
}
