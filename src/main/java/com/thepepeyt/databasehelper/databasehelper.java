package com.thepepeyt.databasehelper;

import com.thepepeyt.databasehelper.BaseTypes.Mysql;
import com.thepepeyt.databasehelper.BaseTypes.Postgresql;
import com.thepepeyt.databasehelper.BaseTypes.Sqlite3;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class databasehelper {


    public static void main(String[] args) throws SQLException {
        File file = new File("database.db");
        Sqlite3 db = databasehelper.Sqlite3builder()
                .file(file)
                .build();

        db.connect();
        System.out.println("Connected with database");

        ArrayList<String> string = new ArrayList<>();
        string.add("UUID STRING");
        string.add("LEVEL INT");
        string.add("MONEY Int");

        db.createTable("player", string);
        System.out.println("Created player table");


        ArrayList<String> into = new ArrayList<>();
        into.add("UUID");
        into.add("LEVEL");
        into.add("MONEY");

        ArrayList<Object> values = new ArrayList<>();
        values.add("2323234324");
        values.add(2);
        values.add(100);


        db.insertInto("player",into,values);
        System.out.println("Inserted");


        ArrayList<String> what = new ArrayList<>();
        values.add("UUID");

        ArrayList<Object> where = new ArrayList<>();
        values.add("2323234324");




        System.out.println(db.getColumn("player", "MONEY", what, where));
        //100

        db.updateColumn("player", "MONEY", what, where, 150);

        System.out.println(db.getColumn("player", "MONEY", what, where));
        //150

        ArrayList<String> xD = new ArrayList<>();
        xD.add("UUID");
        xD.add("LEVEL");

        List<String> leadboard = db.getLeadboard("player", xD, "MONEY");

        leadboard.forEach(x -> System.out.println(x));


        db.disconnect();

    }
    //MADE BY .XMON<3


    //MYSQLBUILDER

    public static MysqlBuilder Mysqlbuilder() {
        return new MysqlBuilder();


    }

    public static Sqlite3Builder Sqlite3builder() {
        return new Sqlite3Builder();


    }

    public static Sqlite3Builder Postgresql() {
        return new Sqlite3Builder();


    }

    public static final class MysqlBuilder {
        private String host;
        private int port = 0;
        private String database;
        private String user;
        private String password;

        public MysqlBuilder host(final String host) {
            this.host = host;
            return this;
        }

        public MysqlBuilder port(final int port) {
            this.port = port;
            return this;
        }

        public MysqlBuilder database(final String database) {
            this.database = database;
            return this;
        }

        public MysqlBuilder user(final String user) {
            this.user = user;
            return this;
        }

        public MysqlBuilder password(final String password) {
            this.password = password;
            return this;
        }


        public Mysql build() {
            if (this.host.isEmpty()) {
                throw new IllegalStateException("Host name cannot be empty");
            }
            if (this.database.isEmpty()) {
                throw new IllegalStateException("Database name cannot be empty");
            }
            if (user.isEmpty()) {
                user = "root";
            }
            if (password.isEmpty()) {
                throw new IllegalStateException("Password cannot be empty");
            }
            if (this.port == 0) {
                port = 3306;
            }
            return new Mysql(database, host, user, password, port);
        }
    }

    public static final class Sqlite3Builder {
        private File file = null;


        public Sqlite3Builder file(final File database) {
            this.file = database;
            return this;
        }


        public Sqlite3 build() {
            if (!this.file.exists() & this.file == null) {
                throw new IllegalStateException("Database file cannot be empty");
            }
            return new Sqlite3(file);
        }
    }

    public static final class PostgresqlBuilder {
        private String host;
        private int port = 0;
        private String database;
        private String user;
        private String password;

        public PostgresqlBuilder host(final String host) {
            this.host = host;
            return this;
        }

        public PostgresqlBuilder port(final int port) {
            this.port = port;
            return this;
        }

        public PostgresqlBuilder database(final String database) {
            this.database = database;
            return this;
        }

        public PostgresqlBuilder user(final String user) {
            this.user = user;
            return this;
        }

        public PostgresqlBuilder password(final String password) {
            this.password = password;
            return this;
        }


        public Postgresql build() {
            if (this.host.isEmpty()) {
                throw new IllegalStateException("Host name cannot be empty");
            }
            if (this.database.isEmpty()) {
                throw new IllegalStateException("Database name cannot be empty");
            }
            if (user.isEmpty()) {
                user = "root";
            }
            if (password.isEmpty()) {
                throw new IllegalStateException("Password cannot be empty");
            }
            if (this.port == 0) {
                port = 3306;
            }
            return new Postgresql(database, host, user, password, port);
        }


    }




}












