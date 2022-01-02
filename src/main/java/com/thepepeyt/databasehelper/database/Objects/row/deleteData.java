package com.thepepeyt.databasehelper.database.Objects.row;

import com.thepepeyt.databasehelper.Utils.DatabaseExceptions;
import com.thepepeyt.databasehelper.Utils.ObservableType;
import com.thepepeyt.databasehelper.database.SQLStatement;
import io.reactivex.rxjava3.core.Observable;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class deleteData {

    private SQLStatement SQL;

    private ObservableType<String> TABLE = new ObservableType<>();

    private ObservableType<String> IDENTIFIERS = new ObservableType<>();

    private ObservableType<Object> VALUES = new ObservableType<>();

    public deleteData(SQLStatement SQL){
        this.SQL=SQL;
    }


    public deleteData where(String identifier, Object value){
        IDENTIFIERS.addValue(identifier);
        VALUES.addValue(value);
        return this;
    }

    public deleteData table(String table){
        TABLE.setData(table);
        return this;
    }

    public Observable<String> getSQLFormula(){
        ObservableType<String> observableType = new ObservableType<>();


        StringBuilder stringBuilder = new StringBuilder();

        TABLE.getObservable().subscribe(table -> {
            if(table == null) throw new DatabaseExceptions("Database table cannot be empty");
            IDENTIFIERS.getObservable().toList().subscribe(identifiers -> {
                stringBuilder.append(SQL.DELETE.replace("{TABLE}", table));
                if(identifiers != null && !identifiers.isEmpty()){
                    stringBuilder.append(" " + identifiers.stream().map(n -> n.replace(n, "WHERE " + n + " =?"))
                            .collect(Collectors.joining(" AND ")));
                }


            });

        });

        observableType.setData(stringBuilder.toString());


        return observableType.getObservable();

    }

    public void executeAsync(){
        getSQLFormula().subscribe(formula -> {
            SQL.preparedStatement(formula, preparedStatement -> {
                VALUES.getObservable().toList().subscribe(values -> {
                    if(!values.isEmpty()){
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
                    }
                    preparedStatement.executeUpdate();

                });
            });
        });
    }

    public void execute() throws SQLException {
        SQL.preparedStatement(getSQLFormula().blockingFirst(), preparedStatement -> {
            try {
                var values = VALUES.getObservable().toList().blockingGet();
                if (!values.isEmpty()) {
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
                }
                preparedStatement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        });


    }

}
