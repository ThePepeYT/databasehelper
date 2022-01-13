package com.thepepeyt.databasehelper.database.Objects.row;

import com.thepepeyt.databasehelper.Utils.ObservableType;
import com.thepepeyt.databasehelper.database.SQLStatement;
import io.reactivex.rxjava3.core.Observable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class getData{

    SQLStatement SQL;



    private ObservableType<String> TABLE = new ObservableType<>();

    private ObservableType<String> COLUMNS = new ObservableType<>();

    private ObservableType<String> IDENTIFIERS = new ObservableType<>();

    private ObservableType<Object> VALUES = new ObservableType<>();

    private ObservableType<String> ORDER = new ObservableType<>();




    public getData(SQLStatement sql) {
        this.SQL = sql;
    }

    public getData table(String table){
        TABLE.setData(table);
        return this;
    }

    public getData columns(String... args){
        COLUMNS.addValue(args);
        return this;
    }

    public getData orderby(String... args){
        ORDER.addValue(args);
        return this;
    }


    public getData where(String identifier, Object value){
        IDENTIFIERS.addValue(identifier);
        VALUES.addValue(value);
        return this;
    }


    public Observable<String> getSQLFormula() {

        ObservableType<String> QUERY = new ObservableType<>();



        StringBuilder stringBuilder = new StringBuilder();

        IDENTIFIERS.getObservable().toList().subscribe(IDENTIFIER -> {
            COLUMNS.getObservable().toList().subscribe(SELECTOR -> {
                if(SELECTOR.isEmpty()) throw new IllegalStateException("Database Columns cannot be empty");
                ORDER.getObservable().toList().subscribe(ORDERBY -> {
                    TABLE.getObservable().subscribe(table -> {
                        if(table == null) throw new IllegalStateException("Database Table cannot be empty");
                        stringBuilder.append(SQL.SELECT_FROM
                                .replace("{TABLE}", table)
                                .replace("{VALUES}", SELECTOR.stream().collect(Collectors.joining(",", "", ""))));
                        if(!IDENTIFIER.isEmpty()) {
                            stringBuilder.append(IDENTIFIER.stream().map(n -> n.replace(n, n + "=?"))
                                    .collect(Collectors.joining(" AND ", "WHERE ", "")));
                        }
                        if(!ORDERBY.isEmpty()){
                            stringBuilder.append(" " + ORDERBY.stream().collect(Collectors.joining(",", "", "")));
                        }
                    });
                });
            });
        });

        QUERY.setData(stringBuilder.toString());

        return QUERY.getObservable();
    }



    public Observable<HashMap<String, Object>> completeAsync(){
        ObservableType<HashMap<String, Object>> observableType = new ObservableType<>();
        getSQLFormula().subscribe(FORMULA -> {
            SQL.preparedStatement(FORMULA, preparedStatement -> {
                IDENTIFIERS.getObservable().toList().subscribe(x -> {
                    COLUMNS.getObservable().toList().subscribe(list -> {

                        if (!x.isEmpty()) {
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
                            });
                        }
                        ResultSet rs = preparedStatement.executeQuery();

                        HashMap<String,Object> map = new HashMap<>();

                        while (rs.next()) {
                            list.forEach(y -> {
                                try {
                                    map.put(y, rs.getObject(y));
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        observableType.setData(map);
                    });
                });
            });
        });


            return observableType.getObservable();
        }


    public HashMap<String,Object> complete(){
        return completeAsync().blockingSingle();
    }






}
