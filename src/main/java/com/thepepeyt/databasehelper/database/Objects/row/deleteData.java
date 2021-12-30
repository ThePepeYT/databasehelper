package com.thepepeyt.databasehelper.database.Objects.row;

import com.thepepeyt.databasehelper.Utils.DatabaseExceptions;
import com.thepepeyt.databasehelper.Utils.ObservableType;
import com.thepepeyt.databasehelper.database.SQLStatement;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class deleteData {

    SQLStatement SQL;

    ObservableType<String> TABLE = new ObservableType<>();

    ObservableType<String> IDENTIFIERS = new ObservableType<>();

    ObservableType<Object> VALUES = new ObservableType<>();

    public deleteData(SQLStatement SQL){
        this.SQL=SQL;
    }

    public deleteData where(String identifier, Float value){
        IDENTIFIERS.addValue(identifier);
        VALUES.addValue(value);
        return this;
    }

    public deleteData where(String identifier, String value){
        IDENTIFIERS.addValue(identifier);
        VALUES.addValue(value);
        return this;
    }

    public deleteData where(String identifier, Integer value){
        IDENTIFIERS.addValue(identifier);
        VALUES.addValue(value);
        return this;
    }

    public deleteData where(String identifier, Long value){
        IDENTIFIERS.addValue(identifier);
        VALUES.addValue(value);
        return this;
    }

    public deleteData where(String identifier, Boolean value){
        IDENTIFIERS.addValue(identifier);
        VALUES.addValue(value);
        return this;
    }

    public deleteData where(String identifier, Double value){
        IDENTIFIERS.addValue(identifier);
        VALUES.addValue(value);
        return this;
    }

    public deleteData table(String table){
        TABLE.setData(table);
        return this;
    }

    public ObservableType<String> getSQLFormula(){
        ObservableType<String> observableType = new ObservableType<>();


        StringBuilder stringBuilder = new StringBuilder();

        TABLE.getObservable().subscribe(table -> {
            if(table == null) throw new DatabaseExceptions("Database table cannot be empty");
            IDENTIFIERS.getObservable().toList().subscribe(identifiers -> {
                stringBuilder.append(SQL.DELETE.replace("{TABLE}", table));
                if(!identifiers.isEmpty()){
                    stringBuilder.append(" " + identifiers.stream().map(n -> n.replace(n, "WHERE " + n + " =?"))
                            .collect(Collectors.joining(" AND ")));
                }


            });

        });

        observableType.setData(stringBuilder.toString());


        return observableType;

    }

    public void completeAsync(){
        getSQLFormula().getObservable().subscribe(formula -> {
            SQL.preparedStatement(formula, preparedStatement -> {
                VALUES.getObservable().toList().subscribe(values -> {
                    if(!values.isEmpty()){
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
                    }
                    preparedStatement.executeUpdate();

                });
            });
        });
    }

    public void complete() throws SQLException {
        SQL.preparedStatement(getSQLFormula().getObservable().blockingFirst(), preparedStatement -> {
            try {
                var values = VALUES.getObservable().toList().blockingGet();
                if (!values.isEmpty()) {
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
                }
                preparedStatement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        });


    }

}
