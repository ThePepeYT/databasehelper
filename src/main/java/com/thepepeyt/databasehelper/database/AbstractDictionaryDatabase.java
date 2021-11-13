package com.thepepeyt.databasehelper.database;

import java.io.File;

public abstract class AbstractDictionaryDatabase extends Statements{
    protected final String host;
    protected final int port;
    protected final String password;
    protected final int timeout;



    protected AbstractDictionaryDatabase(String host, int port, int timeout, String password) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.password = password;



    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;

    }

    public String getPassword() {
        return password;
    }

    public int getTimeout() {
        return timeout;
    }
}
