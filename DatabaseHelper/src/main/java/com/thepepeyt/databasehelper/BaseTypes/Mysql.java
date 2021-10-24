package com.thepepeyt.databasehelper.BaseTypes;

import com.thepepeyt.databasehelper.databasehelper;


import java.sql.*;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Mysql {

    private String host;
    private int port;
    private String database;
    private String user;
    private String password;
    private Connection connection;

    private ArrayList<String> table = new ArrayList<String>();

    public Mysql(String database, String host, String user, String password, int port) {
        this.database = database;
        this.host = host;
        this.user = user;
        this.password = password;
        this.port = port;

        return;
    }

    public Mysql(Mysql mysql) {

        this.database = mysql.database;
        this.host = mysql.getHost();
        this.user = mysql.getUser();
        this.password = mysql.getPassword();
        this.port = mysql.getPort();

        return;


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

    public Connection getConnection() {
        return connection;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
    }


    public void createTable(String table, ArrayList<String> string) {
        PreparedStatement ps = null;
        try {
            StringBuilder something = new StringBuilder();
            string.forEach(x -> something.append("?,"));
            ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + table + "(" + something.substring(0, something.length() - 1) + ")");
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            throw new IllegalStateException("There is table with that name i can't create second :(");
        }


    }

    public void insertInto(String table, ArrayList<String> into, ArrayList<Object> values) throws SQLException {


        if (into.size() != values.size()) {
            throw new IllegalStateException("There should be the same amount of values and column names");
        }
        StringBuilder something = new StringBuilder();
        into.forEach(x -> something.append("?,"));


        PreparedStatement insert = connection.prepareStatement("INSERT INTO " + table + "(" + into.stream().collect(Collectors.joining(",", "", "")) + ") VALUES (" + something.substring(0, something.length() - 1) + ")");
        into.forEach(x -> {
            try {
                insert.setObject(into.indexOf(x) + 1, values.get(into.indexOf(x)));
            } catch (SQLException e) {
                throw new IllegalStateException("The values that you give should be the same that u set in createTable :)");


            }
        });
        insert.executeUpdate();
    }


    public Object getColumn(String table, String column) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table);
            ResultSet results = statement.executeQuery();
            results.next();
            Object result = results.getObject(column);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }


    public Object getColumn(String table, String column, ArrayList<String> where) {


        where.forEach(x -> where.set(where.indexOf(x), "WHERE " + x + " =?"));


        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + String.join(" AND ", where));
            ResultSet results = statement.executeQuery();
            results.next();
            Object result = results.getObject(column);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}












