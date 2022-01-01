package com.thepepeyt.databasehelper.database.Objects.row;

import com.thepepeyt.databasehelper.Utils.DatabaseExceptions;
import com.thepepeyt.databasehelper.Utils.ObservableType;
import com.thepepeyt.databasehelper.database.SQLStatement;
import io.reactivex.rxjava3.core.Observable;

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




    public updateData column(String column, Object value){
        COLUMNS.addValue(column);
        VALUES.addValue(value);
        return this;
    }


    ///
    ///
    ///

    public updateData where(String identifier, Object value){
        IDENTIFIERS.addValue(identifier);
        VALUES.addValue(value);
        return this;
    }


    ///
    ///
    ///

    public Observable<String> getSQLFormula(){
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

        return observableType.getObservable();
    }


    public void executeAsync(){
        getSQLFormula().subscribe(formula -> {
            SQL.preparedStatement(formula, preparedStatement -> {
                IDENTIFIERS.getObservable().toList().subscribe(identifiers -> {
                    VALUES.getObservable().toList().subscribe(values -> {
                        if(!identifiers.isEmpty()) {
                            IVALUES.getObservable().toList().subscribe(ivalues -> {
                                for (int i = 0; i < ivalues.size(); i++) {
                                    Object object = ivalues.get(i);
                                    switch(object.getClass().getCanonicalName()){
                                        case "java.lang.String":
                                            preparedStatement.setString(i + 1, (String) object);
                                            break;
                                        case "java.lang.Integer":
                                            preparedStatement.setInt(i+1, (Integer) object);
                                            break;
                                        case "java.lang.Boolean":
                                            preparedStatement.setBoolean(i+1, (Boolean) object);
                                            break;
                                        case "java.lang.Double":
                                            preparedStatement.setDouble(i+1, (Double) object);
                                            break;
                                        case "java.lang.Float":
                                            preparedStatement.setFloat(i+1, (Float) object);
                                            break;
                                        default:
                                            preparedStatement.setObject(i + 1, object);
                                            break;
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
        SQL.preparedStatement(getSQLFormula().blockingFirst(), preparedStatement -> {
            try {
                var values = VALUES.getObservable().toList().blockingGet();
                var ivalues = IVALUES.getObservable().toList().blockingGet();
                if (!ivalues.isEmpty()) {
                    for (int i = 0; i < ivalues.size(); i++) {
                        Object object = ivalues.get(i);
                        switch(object.getClass().getCanonicalName()){
                            case "java.lang.String":
                                preparedStatement.setString(i + 1, (String) object);
                                break;
                            case "java.lang.Integer":
                                preparedStatement.setInt(i+1, (Integer) object);
                                break;
                            case "java.lang.Boolean":
                                preparedStatement.setBoolean(i+1, (Boolean) object);
                                break;
                            case "java.lang.Double":
                                preparedStatement.setDouble(i+1, (Double) object);
                                break;
                            case "java.lang.Float":
                                preparedStatement.setFloat(i+1, (Float) object);
                                break;
                            default:
                                preparedStatement.setObject(i + 1, object);
                                break;
                        }

                    }
                }
                for (int i = 0; i < values.size(); i++) {
                    Object object = values.get(i);

                    switch(object.getClass().getCanonicalName()){
                        case "java.lang.String":
                            preparedStatement.setString(i + 1, (String) object);
                            break;
                        case "java.lang.Integer":
                            preparedStatement.setInt(i+1, (Integer) object);
                            break;
                        case "java.lang.Boolean":
                            preparedStatement.setBoolean(i+1, (Boolean) object);
                            break;
                        case "java.lang.Double":
                            preparedStatement.setDouble(i+1, (Double) object);
                            break;
                        case "java.lang.Float":
                            preparedStatement.setFloat(i+1, (Float) object);
                            break;
                        default:
                            preparedStatement.setObject(i + 1, object);
                            break;
                    }
                }

                preparedStatement.executeUpdate();

            }catch (SQLException e){
                e.printStackTrace();
            }


        });
    }









}
