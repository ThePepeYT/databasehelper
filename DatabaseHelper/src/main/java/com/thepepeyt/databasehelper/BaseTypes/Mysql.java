package com.thepepeyt.databasehelper.BaseTypes;

import java.sql.*;
import java.util.ArrayList;

import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Mysql {

    private String host;
    private int port;
    private String database;
    private String user;
    private String password;
    private Connection connection;


    public Mysql(String database, String host, String user, String password, int port) {
        this.database = database;
        this.host = host;
        this.user = user;
        this.password = password;
        this.port = port;

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

            ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + table + "(" + string.stream().collect(Collectors.joining(", ", "", "")) + ")");
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void insertInto(String table, ArrayList<String> into, ArrayList<Object> values) throws SQLException {


        if (into.size() != values.size()) {
            Logger.getLogger("There should be the same amount of values and column names");
        }
        StringBuilder something = new StringBuilder();
        into.forEach(x -> something.append("?,"));


        PreparedStatement insert = connection.prepareStatement("INSERT INTO " + table + "(" + into.stream().collect(Collectors.joining(",", "", "")) + ") VALUES (" + something.substring(0, something.length() - 1) + ")");
        into.forEach(x -> {
            try {
                insert.setObject(into.indexOf(x) + 1, values.get(into.indexOf(x)));
            } catch (SQLException e) {
                e.printStackTrace();


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


    public Object getColumn(String table, String column, ArrayList<String> where, ArrayList<Object> what) {


        where.forEach(x -> where.set(where.indexOf(x), "WHERE " + x + " =?"));


        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + String.join(" AND ", where));
            where.forEach(x -> {
                try {
                    statement.setObject(what.indexOf(x) + 1, what.get(what.indexOf(x)));
                } catch (SQLException e) {
                    e.printStackTrace();


                }
            });
            ResultSet results = statement.executeQuery();
            results.next();
            Object result = results.getObject(column);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }


    public void updateColumn(String table, String column, Object something) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE " + table + " SET " + column + "=?");
            statement.setObject(1, something);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateColumn(String table, String column, ArrayList<String> where, ArrayList<Object> what, Object something) throws SQLException {


        where.forEach(x -> where.set(where.indexOf(x), "WHERE " + x + " =?"));
        PreparedStatement statement = connection.prepareStatement("UPDATE " + table + " SET " + column + "=? " + String.join(" AND ", where));
        statement.setObject(1, something);
        what.forEach(x -> {
            try {
                statement.setObject(what.indexOf(x) + 2, what.get(what.indexOf(x)));
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
        statement.executeUpdate();
    }
}












