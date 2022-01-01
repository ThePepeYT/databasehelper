package com.thepepeyt.databasehelper.database.Objects.column;

import com.thepepeyt.databasehelper.Utils.DatabaseExceptions;
import com.thepepeyt.databasehelper.Utils.ObservableType;
import com.thepepeyt.databasehelper.database.SQLStatement;
import io.reactivex.rxjava3.core.Observable;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class deleteColumn {

    SQLStatement SQL;

    private ObservableType<String> TABLE = new ObservableType<>();

    private ObservableType<String> COLUMNS = new ObservableType<>();



    public deleteColumn(SQLStatement SQL){
        this.SQL = SQL;
    }

    public deleteColumn table(String table){
        TABLE.setData(table);
        return this;
    }

    public deleteColumn columns(String... args){
        COLUMNS.addValue(args);
        return this;
    }

    public Observable<String> getSQLFormula(){
        ObservableType<String> observableType = new ObservableType<>();

        TABLE.getObservable().subscribe(table -> {
            COLUMNS.getObservable().toList().subscribe(columns -> {
                if(TABLE == null || columns.isEmpty()) throw new DatabaseExceptions("Table or Columns cannot be empty");
                observableType.setData(SQL.REMOVE_COLUMN.replace("{TABLE}", table).replace("{COLUMNS}", columns.stream().collect(Collectors.joining(","))));
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
