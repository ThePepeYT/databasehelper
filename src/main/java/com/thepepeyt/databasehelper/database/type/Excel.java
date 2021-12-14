package com.thepepeyt.databasehelper.database.type;

import com.thepepeyt.databasehelper.database.AbstractLiteDatabase;
import com.thepepeyt.databasehelper.database.AbstractSQLDatabase;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Excel extends AbstractLiteDatabase {

    public Excel(final File file) {
        super(file);
    }

    @Override
    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:odbc:" + file);
    }
}
