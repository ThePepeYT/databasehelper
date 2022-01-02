package com.thepepeyt.databasehelper.database.Objects.row;

import com.thepepeyt.databasehelper.Utils.DatabaseExceptions;
import com.thepepeyt.databasehelper.Utils.ObservableType;
import com.thepepeyt.databasehelper.database.SQLStatement;
import io.reactivex.rxjava3.core.Observable;

import java.sql.ResultSet;
import java.util.stream.Collectors;

public class checkData {

    private SQLStatement SQL;
    private ObservableType<String> TABLE = new ObservableType<>();
    private ObservableType<String> IDENTIFIERS = new ObservableType<>();
    private ObservableType<Object> VALUES = new ObservableType<>();

    public checkData(SQLStatement SQL){
        this.SQL = SQL;
    }

    public checkData where(String identifier, Object value){
        IDENTIFIERS.addValue(identifier);
        VALUES.addValue(value);
        return this;
    }

    public checkData table(String table){
        TABLE.setData(table);
        return this;
    }

    public Observable<String> getSQLFormula(){
        ObservableType<String> observableType = new ObservableType<>();
        TABLE.getObservable().subscribe(table -> {
            if(table == null) throw new DatabaseExceptions("Database table cannot be empty");
            IDENTIFIERS.getObservable().toList().subscribe(identifiers -> {
                if(identifiers == null || identifiers.isEmpty()) throw new DatabaseExceptions("There should be at least 1 identifier");
                observableType.setData(SQL.SELECT_FROM
                        .replace("{TABLE}", table) + identifiers.stream().map(n -> n.replace(n, "WHERE " + n + " =?"))
                        .collect(Collectors.joining(" AND ")));

            });
        });


        return observableType.getObservable();
    }


    public Observable<Boolean> completeAsync(){
        ObservableType<Boolean> observableType = new ObservableType<>();
        getSQLFormula().subscribe(formula -> {
            SQL.preparedStatement(formula, preparedStatement -> {
                VALUES.getObservable().toList().subscribe(values -> {
                    for (int i = 0; i < values.size(); i++) {
                        Object object = values.get(i);
                        switch (object.getClass().getCanonicalName()) {
                            case "java.lang.String":
                                preparedStatement.setString(i + 1, (String) object);
                                break;
                            case "java.lang.Integer":
                                preparedStatement.setInt(i + 1, (Integer) object);
                                break;
                            case "java.lang.Boolean":
                                preparedStatement.setBoolean(i + 1, (Boolean) object);
                                break;
                            case "java.lang.Double":
                                preparedStatement.setDouble(i + 1, (Double) object);
                                break;
                            case "java.lang.Float":
                                preparedStatement.setFloat(i + 1, (Float) object);
                                break;
                            default:
                                preparedStatement.setObject(i + 1, object);
                                break;
                        }
                    }

                    ResultSet rs = preparedStatement.executeQuery();
                    if(rs.next()){
                        observableType.setData(true);
                    }
                    else{
                        observableType.setData(false);
                    }
                });

            });
        });

        return observableType.getObservable();

    }


    public Boolean complete(){
        return completeAsync().blockingSingle();
    }
}
