package com.thepepeyt.databasehelper.database.Objects.row;

import com.thepepeyt.databasehelper.Utils.DatabaseExceptions;
import com.thepepeyt.databasehelper.Utils.ObservableType;
import com.thepepeyt.databasehelper.database.SQLStatement;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class updateData {

    SQLStatement SQL;


    private ObservableType<String> TABLE = new ObservableType<>();

    private ObservableType<String> COLUMNS = new ObservableType<>();

    private ObservableType<Object> VALUES = new ObservableType<>();

    private ObservableType<String> IDENTIFIERS = new ObservableType<>();

    private ObservableType<Object> IVALUES = new ObservableType<>();



    public updateData(SQLStatement SQL){
        this.SQL = SQL;
    }



    public updateData table(String table){
        TABLE.setData(table);
        return this;
    }




    public updateData column(String column, int value){
        COLUMNS.addValue(column);
        VALUES.addValue(value);
        return this;
    }

    public updateData column(String column, String value){
        COLUMNS.addValue(column);
        VALUES.addValue(value);
        return this;
    }

    public updateData column(String column, Long value){
        COLUMNS.addValue(column);
        VALUES.addValue(value);
        return this;
    }

    public updateData column(String column, Float value){
        COLUMNS.addValue(column);
        VALUES.addValue(value);
        return this;
    }


    ///
    ///
    ///


    public updateData where(String identifier, Float value){
        IDENTIFIERS.addValue(identifier);
        IVALUES.addValue(value);
        return this;
    }

    public updateData where(String identifier, String value){
        IDENTIFIERS.addValue(identifier);
        IVALUES.addValue(value);
        return this;
    }

    public updateData where(String identifier, Integer value){
        IDENTIFIERS.addValue(identifier);
        IVALUES.addValue(value);
        return this;
    }

    public updateData where(String identifier, Long value){
        IDENTIFIERS.addValue(identifier);
        IVALUES.addValue(value);
        return this;
    }

    public updateData where(String identifier, Boolean value){
        IDENTIFIERS.addValue(identifier);
        IVALUES.addValue(value);
        return this;
    }

    public updateData where(String identifier, Double value){
        IDENTIFIERS.addValue(identifier);
        VALUES.addValue(value);
        return this;
    }


    ///
    ///
    ///

    public ObservableType<String> getSQLFormula(){
        ObservableType<String> observableType = new ObservableType<>();


        StringBuilder stringBuilder = new StringBuilder();

        TABLE.getObservable().subscribe(table -> {
            if(table == null) throw new IllegalStateException("Database Table cannot be empty");
            COLUMNS.getObservable().toList().subscribe(columns -> {
                if(columns.isEmpty()) throw new DatabaseExceptions("Number of columns should be greater than 0 ");
                    IDENTIFIERS.getObservable().toList().subscribe(identifiers -> {
                            stringBuilder.append(SQL.UPDATE_COLUMN
                                    .replace("{TABLE}", table)
                                    .replace("{COLUMNS}", columns.stream().map(n -> n.replace(n, n + "=?")).collect(Collectors.joining(", "))));

                            if(!identifiers.isEmpty()) stringBuilder.append(" " + identifiers.stream().map(n -> n.replace(n, "WHERE " + n + " =?"))
                                    .collect(Collectors.joining(" AND ")));


                        });

                    });

                });

        observableType.setData(stringBuilder.toString());

        return observableType;
    }


    public void executeAsync(){
        getSQLFormula().getObservable().subscribe(formula -> {
            SQL.preparedStatement(formula, preparedStatement -> {
                IDENTIFIERS.getObservable().toList().subscribe(identifiers -> {
                    VALUES.getObservable().toList().subscribe(values -> {
                        if(!identifiers.isEmpty()) {
                            IVALUES.getObservable().toList().subscribe(ivalues -> {
                                for (int i = 0; i < ivalues.size(); i++) {
                                    Object object = ivalues.get(i);
                                    if (object instanceof String)
                                        preparedStatement.setString(i + 1 + values.size(), (String) object);
                                    else if (object instanceof Integer)
                                        preparedStatement.setInt(i + 1 + values.size(), (Integer) object);
                                    else if (object instanceof Boolean)
                                        preparedStatement.setBoolean(i + 1 + values.size(), (Boolean) object);
                                    else if (object instanceof Float)
                                        preparedStatement.setFloat(i + 1 + values.size(), (Float) object);
                                    else {
                                        preparedStatement.setObject(i + 1 + values.size(), object);

                                    }
                                }

                            });
                        }

                        for (int i = 0; i < values.size(); i++) {
                            Object object = values.get(i);
                            if (object instanceof String)
                                preparedStatement.setString(i + 1, (String) object);
                            else if (object instanceof Integer)
                                preparedStatement.setInt(i + 1, (Integer) object);
                            else if (object instanceof Boolean)
                                preparedStatement.setBoolean(i + 1, (Boolean) object);
                            else if (object instanceof Float)
                                preparedStatement.setFloat(i + 1, (Float) object);
                            else {
                                preparedStatement.setObject(i, object);

                            }
                        }

                        preparedStatement.executeUpdate();

                    });
                });
            });

        });

    }

    public void execute() throws SQLException {
        SQL.preparedStatement(getSQLFormula().getObservable().blockingFirst(), preparedStatement -> {
            try {
                var values = VALUES.getObservable().toList().blockingGet();
                var ivalues = IVALUES.getObservable().toList().blockingGet();
                if (!ivalues.isEmpty()) {
                    for (int i = 0; i < ivalues.size(); i++) {
                        Object object = ivalues.get(i);
                        if (object instanceof String)
                            preparedStatement.setString(i + 1 + values.size(), (String) object);
                        else if (object instanceof Integer)
                            preparedStatement.setInt(i + 1 + values.size(), (Integer) object);
                        else if (object instanceof Boolean)
                            preparedStatement.setBoolean(i + 1 + values.size(), (Boolean) object);
                        else if (object instanceof Float)
                            preparedStatement.setFloat(i + 1 + values.size(), (Float) object);
                        else {
                            preparedStatement.setObject(i + 1 + values.size(), object);
                        }
                    }
                }
                for (int i = 0; i < values.size(); i++) {
                    Object object = values.get(i);
                    if (object instanceof String)
                        preparedStatement.setString(i + 1, (String) object);
                    else if (object instanceof Integer)
                        preparedStatement.setInt(i + 1, (Integer) object);
                    else if (object instanceof Boolean)
                        preparedStatement.setBoolean(i + 1, (Boolean) object);
                    else if (object instanceof Float)
                        preparedStatement.setFloat(i + 1, (Float) object);
                    else {
                        preparedStatement.setObject(i, object);

                    }
                }

                preparedStatement.executeUpdate();

            }catch (SQLException e){
                e.printStackTrace();
            }


        });
    }









}
