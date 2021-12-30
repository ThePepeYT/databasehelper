package com.thepepeyt.databasehelper.database.Objects.row;

import com.thepepeyt.databasehelper.Utils.ObservableType;
import com.thepepeyt.databasehelper.database.SQLStatement;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class insertData {

    SQLStatement SQL;



    private ObservableType<String> TABLE = new ObservableType<>();

    private ObservableType<String> COLUMNS = new ObservableType<>();

    private ObservableType<Object> VALUES = new ObservableType<>();


    public insertData(SQLStatement SQL){
        this.SQL = SQL;
    }

    public insertData table(String table){
        TABLE.setData(table);
        return this;
    }

    public insertData insert(String column, int value){
        COLUMNS.addValue(column);
        VALUES.addValue(value);
        return this;
    }

    public insertData insert(String column, String value){
        COLUMNS.addValue(column);
        VALUES.addValue(value);
        return this;
    }

    public insertData insert(String column, Long value){
        COLUMNS.addValue(column);
        VALUES.addValue(value);
        return this;
    }

    public insertData insert(String column, Float value){
        COLUMNS.addValue(column);
        VALUES.addValue(value);
        return this;
    }





    public ObservableType<String> getSQLFormula() {

        ObservableType<String> QUERY = new ObservableType<>();

        COLUMNS.getObservable().toList()
                .filter(columns -> columns.isEmpty())
                .subscribe(columns -> {
                    throw new IllegalStateException("Columns can`t be empty");

                });

        TABLE.getObservable()
                .filter(table -> table == null)
                .subscribe(table -> {
                    throw new IllegalStateException("Table can`t be empty");
                });

        StringBuilder stringBuilder = new StringBuilder();

        COLUMNS.getObservable().toList().subscribe(columns -> {
            if(columns.isEmpty()) throw new IllegalStateException("Database Columns cannot be empty");
            VALUES.getObservable().toList().subscribe(values -> {
                TABLE.getObservable().subscribe(table -> {
                    if(table == null) throw new IllegalStateException("Database Table cannot be empty");

                    stringBuilder.append(SQL.INSERT_INTO
                            .replace("{TABLE}", table)
                            .replace("{INTO}", columns.stream().collect(Collectors.joining(",")))
                            .replace("{VALUES}", columns.stream().map(n -> n.replace(n, "?")).collect(Collectors.joining(","))));

                });
            });
        });

        QUERY.setData(stringBuilder.toString());

        return QUERY;
    }


    public void executeAsync(){
        getSQLFormula().getObservable().subscribe(formula -> {
            VALUES.getObservable().toList().subscribe(values -> {
                SQL.preparedStatement(formula, preparedStatement -> {
                    try {
                        for (int i = 0; i < values.size(); i++) {
                            Object object = values.get(i);

                            if (object instanceof String) preparedStatement.setString(i + 1, (String) object);
                            else if (object instanceof Integer) preparedStatement.setInt(i + 1, (Integer) object);
                            else if (object instanceof Boolean) preparedStatement.setBoolean(i + 1, (Boolean) object);
                            else if (object instanceof Float) preparedStatement.setFloat(i + 1, (Float) object);
                            else {
                                preparedStatement.setObject(i + 1, object);

                            }
                        }


                        preparedStatement.executeUpdate();
                    }
                    catch (SQLException e){
                        e.printStackTrace();
                    }
                });

            });
        });
    }

    public void execute() throws SQLException {
        SQL.preparedStatement(getSQLFormula().getObservable().blockingFirst(), preparedStatement -> {
            var values = VALUES.getObservable().toList().blockingGet();
            try {
                for (int i = 0; i < values.size(); i++) {
                    Object object = values.get(i);

                    if (object instanceof String) preparedStatement.setString(i + 1, (String) object);
                    else if (object instanceof Integer) preparedStatement.setInt(i + 1, (Integer) object);
                    else if (object instanceof Boolean) preparedStatement.setBoolean(i + 1, (Boolean) object);
                    else if (object instanceof Float) preparedStatement.setFloat(i + 1, (Float) object);
                    else {

                        preparedStatement.setObject(i + 1, object);
                    }

                }

                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

}
