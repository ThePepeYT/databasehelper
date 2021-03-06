package com.thepepeyt.databasehelper.database.Objects.table;

import com.thepepeyt.databasehelper.Utils.DatabaseExceptions;
import com.thepepeyt.databasehelper.Utils.ObservableType;
import com.thepepeyt.databasehelper.database.SQLStatement;
import io.reactivex.rxjava3.core.Observable;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class createTable {

    private SQLStatement SQL;
    private ObservableType<String> TABLE = new ObservableType<>();
    private ObservableType<String> COLUMNS = new ObservableType<>();

    public createTable(SQLStatement SQL){
        this.SQL = SQL;
    }

    public createTable table(String table){
        TABLE.setData(table);
        return this;
    }

    public createTable columns(String... columns){
        for (String column : columns) {
            COLUMNS.addValue(column);
        }
        return this;
    }

    public Observable<String> getSQLFormula(){
        ObservableType<String> observableType = new ObservableType<>();
        TABLE.getObservable().subscribe(table -> {
            if(TABLE == null) throw new DatabaseExceptions("Table cannot be empty");
            COLUMNS.getObservable().toList().subscribe(columns -> {
                observableType.setData(SQL.CREATE_TABLE
                        .replace("{TABLE}", table)
                        .replace("{COLUMNS}", columns.stream().collect(Collectors.joining(",")))
                );
            });

        });
        return observableType.getObservable();
    }

    public void executeAsync(){
        getSQLFormula().subscribe(formula -> {
            SQL.preparedStatement(formula, preparedStatement -> {
                try {
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public void execute(){
        try {
            SQL.preparedStatement(getSQLFormula().blockingSingle(), preparedStatement -> {
                try {
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
