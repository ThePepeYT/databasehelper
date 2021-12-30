package com.thepepeyt.databasehelper.database;

import com.thepepeyt.databasehelper.database.Objects.column.addColumn;
import com.thepepeyt.databasehelper.database.Objects.column.deleteColumn;
import com.thepepeyt.databasehelper.database.Objects.row.deleteData;
import com.thepepeyt.databasehelper.database.Objects.row.getData;
import com.thepepeyt.databasehelper.database.Objects.row.insertData;
import com.thepepeyt.databasehelper.database.Objects.row.updateData;
import com.thepepeyt.databasehelper.database.Objects.table.createTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;

public class SQLStatement implements DatabaseConnection {

    ///ROWS
    public final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            "{TABLE} ({COLUMNS})";
    public final String INSERT_INTO = "INSERT INTO {TABLE} ({INTO}) " +
            "VALUES ({VALUES})";
    public final String UPDATE_COLUMN = "UPDATE {TABLE} SET {COLUMNS}";
    public final String SELECT_FROM = "SELECT * FROM {TABLE} ";
    public final String LEADBOARD = "SELECT {VALUES} from {TABLE} ORDER BY {ORDER} DESC LIMIT {LIMIT}";
    public final String DELETE = "DELETE FROM {TABLE}";
    ///COLUMNS
    public final String ADD_COLUMN = "ALTER TABLE {TABLE} ADD {COLUMNS}";
    public final String REMOVE_COLUMN = "ALTER TABLE {TABLE} DROP {COLUMNS};";


    protected Connection connection;

    public void setConnection(final Connection connection) {
        this.connection = connection;
    }


    @Override
    public void connect() throws SQLException, ClassNotFoundException {}

    @Override
    public void disconnect() {
        try {
            if(!connection.isClosed() || connection != null){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.connection = null;
    }

    public void preparedStatement(final String sql, final Consumer<PreparedStatement> consumer) throws SQLException {
        if(connection == null){
            throw new NullPointerException("Connection cannot be null");
        }
        try (final PreparedStatement ps = connection.prepareStatement(sql)) {
            consumer.accept(ps);
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
    }



    public Connection getConnection() {
        return connection;
    }


    ///ROW
    public getData getData(){
        return new getData(this);
    }
    public insertData insertData(){
        return new insertData(this);
    }
    public updateData updateData(){
        return new updateData(this);
    }
    public deleteData deleteData() { return new deleteData(this);}
    ///TABLE
    public createTable createTable() {return new createTable(this);}
    ///COLUMNS
    public addColumn addColumn() {return new addColumn(this);}
    public deleteColumn deleteColumn() {return new deleteColumn(this);}

}
