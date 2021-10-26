package com.thepepeyt.databasehelper.database;

public abstract class AbstractSQLDatabase extends SQLStatement {
    protected final String host;
    protected final int port;
    protected final String database;
    protected final String user;
    protected final String password;

    protected AbstractSQLDatabase(final String database, final String host,
     final String user,
           final String password, final int port) {
        this.database = database;
        this.host = host;
        this.user = user;
        this.password = password;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
