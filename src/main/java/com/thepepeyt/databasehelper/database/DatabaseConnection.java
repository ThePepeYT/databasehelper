package com.thepepeyt.databasehelper.database;

import com.thepepeyt.databasehelper.Utils.DatabaseExceptions;

import java.sql.SQLException;

public interface DatabaseConnection {
    void connect() throws SQLException, ClassNotFoundException, DatabaseExceptions;
    void disconnect();
}
