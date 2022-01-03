package com.thepepeyt.databasehelper.database.type;

import com.thepepeyt.databasehelper.Utils.DatabaseExceptions;
import com.thepepeyt.databasehelper.database.AbstractLiteDatabase;
import com.thepepeyt.databasehelper.database.SQLStatement;

import java.io.File;
import java.sql.*;

public class SQLite3 extends AbstractLiteDatabase {
    public SQLite3(final File file) {
        super(file);
    }

    @Override
    public void connect() throws DatabaseExceptions {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + file);
        } catch (SQLException | ClassNotFoundException throwables) {
            throw new DatabaseExceptions("You do not have the driver for this database installed");
        }
    }
}

