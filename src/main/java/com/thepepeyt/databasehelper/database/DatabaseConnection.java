package com.thepepeyt.databasehelper.database;

import java.sql.SQLException;

public interface DatabaseConnection {
    void connect() throws SQLException, ClassNotFoundException;
    void disconnect();
}
