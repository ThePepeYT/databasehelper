
package com.thepepeyt.databasehelper.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SQLStatement implements DatabaseConnection {
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            "{TABLE} ({COLUMNS})";
    private static final String INSERT_INTO = "INSERT INTO {TABLE} ({INTO}) " +
            "VALUES ({VALUES})";
    private static final String UPDATE_COLUMN = "UPDATE {TABLE} SET {COLUMN}=?";
    private static final String SELECT_FROM = "SELECT * FROM {TABLE} ";
    static final String WHERE = "WHERE {WHAT} =? AND";
    private static final String EXISTS = "SELECT * FROM {TABLE}";
    private static final String LEADBOARD = "SELECT {VALUES} from {TABLE} ORDER BY {ORDER} DESC LIMIT {LIMIT}";
	
    private static final String DELETE = "DELETE FROM {TABLE}";


    protected Connection connection;

    public void setConnection(final Connection connection) {
        this.connection = connection;
    }


    public void createTable(final String table, final List<String> string) throws SQLException {
        Executors.newCachedThreadPool().execute(() -> {
            try {
                preparedStatement(CREATE_TABLE.replace("{TABLE}", table).replace(
                        "{COLUMNS}", string.stream().collect(Collectors
                                .joining(", ", "", ""))), preparedStatement -> {
                    try {
                        preparedStatement.execute();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void insertInto(final String table, final List<String> into,
                           List<Object> values) throws SQLException {
        Executors.newCachedThreadPool().execute(() -> {
            if (into.size() != values.size()) {
                Logger.getLogger("There should be the same amount of values and column names");
            }
            StringBuilder something = new StringBuilder();
            into.forEach(x -> something.append("?,"));

            try {
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public Object getColumn(final String table, final String column) throws SQLException {
        final Object[] object = new Object[1];


        final CompletableFuture<Object> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {


            try {
                preparedStatement(SELECT_FROM.replace("{TABLE}", table),
                        preparedStatement -> {
                            try {
                                ResultSet results = preparedStatement.executeQuery();
                                if (results.next()) {
                                    completableFuture.complete(results.getObject(column));
                                }
                            } catch (SQLException ex) {
                                completableFuture.complete(null);
                                ex.printStackTrace();

                            }
                        });
            } catch (SQLException e) {
                completableFuture.complete(null);
                e.printStackTrace();
            }

        });
        return completableFuture;
    }





    public CompletableFuture<Object> getColumn(final String table, final String column,
                            final List<String> where,
                            final List<Object> what) throws SQLException {
        CompletableFuture<Object> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            if (where.size() != what.size()) {
                Logger.getLogger("There should be the same amount of values and column names");
            }
            where.forEach(x -> where.set(where.indexOf(x), "WHERE " + x + " =?"));

            try {
                preparedStatement(SELECT_FROM.replace("{TABLE}", table) + String.join(" AND ", where), preparedStatement -> {
                    try {
                        where.forEach(x -> {
                            try {
                                preparedStatement.setObject(what.indexOf(x) + 1, what.get(what.indexOf(x)));
                            } catch (SQLException e) {
                                completableFuture.complete(null);
                                e.printStackTrace();
                            }
                        });
                        ResultSet results = preparedStatement.executeQuery();
                        if (results.next()) {
                            completableFuture.complete(results.getObject(column));
                        }
                    } catch (SQLException ex) {
                        completableFuture.complete(null);
                        ex.printStackTrace();
                    }
                });
            } catch (SQLException e) {
                completableFuture.complete(null);
                e.printStackTrace();
            }
        });
        return completableFuture;
    }

    public void updateColumn(final String table, final String column,
                             final Object something) {
        Executors.newCachedThreadPool().execute(() -> {
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
        });
    }

    public void updateColumn(final String table, final String column,
                             final List<String> where, final List<Object> what,
                             final Object something) throws SQLException {
        Executors.newCachedThreadPool().execute(() -> {
            if (where.size() != what.size()) {
                Logger.getLogger("There should be the same amount of values and column names");
            }

            where.forEach(x -> where.set(where.indexOf(x), WHERE.replace("{WHAT}", x)));
            try {
                preparedStatement(UPDATE_COLUMN.replace("{TABLE}", table).replace("{COLUMN}", column) + String.join(" AND ", where), preparedStatement -> {
                    try {
                        preparedStatement.setObject(1, something);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    what.forEach(x -> {
                        try {
                            preparedStatement.setObject(what.indexOf(x) + 2, what.get(what.indexOf(x)));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                    try {
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<Boolean> ifExists(final String table, final List<String> where, final List<Object> what) throws SQLException {
        final CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            if (where.size() != what.size()) {
                Logger.getLogger("There should be the same amount of values and column names");
            }

            where.forEach(x -> where.set(where.indexOf(x), WHERE.replace("{WHAT}", x)));
            try {
                preparedStatement(EXISTS.replace("{TABLE}", table) + String.join(" AND ", where), preparedStatement -> {
                    try {

                        what.forEach(x -> {
                            try {
                                preparedStatement.setObject(what.indexOf(x) + 1, what.get(what
                                        .indexOf(x)));
                            } catch (SQLException e) {
                                e.printStackTrace();
                                completableFuture.complete(false);
                            }
                        });
                        ResultSet results = preparedStatement.executeQuery();
                        if (results.next()) {
                            completableFuture.complete(true);

                        }
                        completableFuture.complete(false);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        completableFuture.complete(false);


                    }
                });
            } catch (SQLException e) {
                e.printStackTrace();
                completableFuture.complete(false);
            }
        });



        return completableFuture;
    }

    public CompletableFuture<List<List<Object>>> getLeadboard(String table, int limit, String orderBY, List<String> into) throws SQLException {
        final CompletableFuture<List<List<Object>>> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            List<List<Object>> list = new ArrayList<>();


            try {
                preparedStatement(LEADBOARD
                        .replace("{TABLE}", table)
                        .replace("{LIMIT}", String.valueOf(limit))
                        .replace("{VALUES}", into.stream().collect(Collectors.joining(",", "", "")))
                        .replace("{ORDER}", orderBY), statement -> {
                    try {
                        ResultSet rs = statement.executeQuery();
                        while (rs.next()) {
                            List<Object> playerlist = new ArrayList<>();
                            into.forEach(x -> {
                                try {
                                    playerlist.add(rs.getObject(into.indexOf(x) + 1));
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    completableFuture.complete(null);
                                }
                            });
                            list.add(playerlist);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        completableFuture.complete(null);
                    }


                });
            } catch (SQLException e) {
                e.printStackTrace();
                completableFuture.complete(null);
            }
            completableFuture.complete(list);
        });

        return completableFuture;
    }

    public void deleteFrom(String table, List<String> where, List<Object> what){
        Executors.newCachedThreadPool().execute(() -> {
 
            where.forEach(x -> where.set(where.indexOf(x), "WHERE " + x + " =?"));
 
            try {
 
 
 
 
                preparedStatement(DELETE
                                .replace("{TABLE}", table) + " " + String.join(" AND ", where)
                        , preparedStatement -> {
                            what.forEach(x -> {
                                try {
                                    preparedStatement.setObject(what.indexOf(x) + 1, what.get(what
                                            .indexOf(x)));
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            });
                            try {
                                preparedStatement.executeUpdate();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });
            } catch (SQLException e) {
                e.printStackTrace();
            }
 
 
        });
 
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
