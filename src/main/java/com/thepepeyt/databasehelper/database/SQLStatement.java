package com.thepepeyt.databasehelper.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SQLStatement implements DatabaseConnection {
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            "{TABLE} ({COLUMNS})";
    private static final String INSERT_INTO = "INSERT INTO {TABLE} ({INTO}) " +
            "VALUES ({VALUES})";
    private static final String SELECT_FROM = "SELECT * FROM {TABLE}";
    private static final String UPDATE_COLUMN = "UPDATE {TABLE} SET {COLUMN}=?";
    private static final String WHERE = "WHERE {WHAT} =?";
    private static final String EXISTS = "SELECT * FROM {TABLE}";


    protected Connection connection;

    public void setConnection(final Connection connection) {
        this.connection = connection;
    }


    public void createTable(final String table, final ArrayList<String> string) throws SQLException {
        preparedStatement(CREATE_TABLE.replace("{TABLE}", table).replace(
                "{COLUMNS}", string.stream().collect(Collectors
                        .joining(", ", "", ""))), preparedStatement -> {
            try {
                preparedStatement.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void insertInto(final String table, final ArrayList<String> into,
                           ArrayList<Object> values) throws SQLException {
        if (into.size() != values.size()) {
            Logger.getLogger("There should be the same amount of values and column names");
        }
        StringBuilder something = new StringBuilder();
        into.forEach(x -> something.append("?,"));

        preparedStatement(INSERT_INTO.replace("{TABLE}", table)
                .replace("{INTO}", into.stream().collect(Collectors.joining(",", "", "")) + "")
                .replace("{VALUES}", something.substring(0, something.length() - 1)), preparedStatement -> {
            try {
                into.forEach(x -> {
                    try {
                        preparedStatement.setObject(into.indexOf(x) + 1, values.get(into
                                .indexOf(x)));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                preparedStatement.executeUpdate();
            } catch (final SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    public Object getColumn(final String table, final String column) throws SQLException {
        final Object[] object = new Object[1];

        preparedStatement(SELECT_FROM.replace("{TABLE}", table),
                preparedStatement -> {
                    try {
                        ResultSet results = preparedStatement.executeQuery();
                        if (results.next()) {
                            object[0] = results.getObject(column);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });
        return object[0];
    }


    public Object getColumn(final String table, final String column,
                            final ArrayList<String> where, final ArrayList<Object> what) throws SQLException {
        final Object[] object = new Object[1];
        if (where.size() != what.size()) {
            Logger.getLogger("There should be the same amount of values and column names");
        }
        where.forEach(x -> where.set(where.indexOf(x), "WHERE " + x + " =?"));

        preparedStatement(SELECT_FROM.replace("{TABLE}", table) + String.join(" AND ", where), preparedStatement -> {
            try {
                where.forEach(x -> {
                    try {
                        preparedStatement.setObject(what.indexOf(x) + 1, what.get(what.indexOf(x)));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                ResultSet results = preparedStatement.executeQuery();
                if (results.next()) {
                    object[0] = results.getObject(column);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        return object[0];
    }

    public void updateColumn(final String table, final String column,
                             final Object something) {
        try {
            preparedStatement(UPDATE_COLUMN.replace("{TABLE}", table).replace("{COLUMN}", column), preparedStatement -> {
                try {
                    preparedStatement.setObject(1, something);
                    preparedStatement.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateColumn(final String table, final String column,
                             final ArrayList<String> where, final ArrayList<Object> what,
                             final Object something) throws SQLException {
        if (where.size() != what.size()) {
            Logger.getLogger("There should be the same amount of values and column names");
        }

        where.forEach(x -> where.set(where.indexOf(x), WHERE.replace("{WHAT}", x)));
        preparedStatement(UPDATE_COLUMN.replace("{TABLE}", table).replace("{COLUMN}", column) + String.join(" AND ", where), preparedStatement -> {
            try {
                preparedStatement.setObject(1, something);

                what.forEach(x -> {
                    try {
                        preparedStatement.setObject(what.indexOf(x) + 2, what.get(what
                                .indexOf(x)));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    public boolean ifExists(final String table, final ArrayList<String> where, final ArrayList<Object> what) throws SQLException {
        final Boolean[] exists = {false};
        if (where.size() != what.size()) {
            Logger.getLogger("There should be the same amount of values and column names");
        }

        where.forEach(x -> where.set(where.indexOf(x), WHERE.replace("{WHAT}", x)));
        preparedStatement(EXISTS.replace("{TABLE}", table) + String.join(" AND ", where), preparedStatement -> {
            try {

                what.forEach(x -> {
                    try {
                        preparedStatement.setObject(what.indexOf(x) + 1, what.get(what
                                .indexOf(x)));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                ResultSet results = preparedStatement.executeQuery();
                if(results.next()){
                    exists[0] = true;

                }
            } catch (SQLException ex) {
                ex.printStackTrace();


            }
        });
    return exists[0];
    }



    @Override
    public void connect() throws SQLException, ClassNotFoundException {}

    @Override
    public void disconnect() {
        try {
            if(!connection.isClosed() || connection != null){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.connection = null;
    }

    private void preparedStatement(final String sql, final Consumer<PreparedStatement> consumer) throws SQLException {
        if(connection == null){
            throw new NullPointerException("Connection cannot be null");
        }
        try (final PreparedStatement ps = connection.prepareStatement(sql)) {
            consumer.accept(ps);
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
