package com.thepepeyt.databasehelper.database.Objects.column;

import com.thepepeyt.databasehelper.Utils.DatabaseExceptions;
import com.thepepeyt.databasehelper.Utils.ObservableType;
import com.thepepeyt.databasehelper.database.SQLStatement;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class addColumn {

    SQLStatement SQL;

    private ObservableType<String> TABLE = new ObservableType<>();

    private ObservableType<String> COLUMNS = new ObservableType<>();



    public addColumn(SQLStatement SQL){
        this.SQL = SQL;
    }

    public addColumn table(String table){
        TABLE.setData(table);
        return this;
    }

    public addColumn columns(String... args){
        COLUMNS.addValue(args);
        return this;
    }

    public Observable<String> getSQLFormula(){
        ObservableType<String> observableType = new ObservableType<>();

        TABLE.getObservable().subscribe(table -> {
            COLUMNS.getObservable().toList().subscribe(columns -> {
                if(TABLE == null || columns.isEmpty()) throw new DatabaseExceptions("Table or Columns cannot be empty");
                observableType.setData(SQL.ADD_COLUMN.replace("{TABLE}", table).replace("{COLUMNS}", columns.stream().collect(Collectors.joining(","))));
            });

        });
        return observableType.getObservable();
    }

    public void completeAsync(){
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

    public void complete() throws SQLException {
        SQL.preparedStatement(getSQLFormula().blockingFirst(), preparedStatement -> {
            try {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }



}
