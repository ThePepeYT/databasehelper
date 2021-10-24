package com.thepepeyt.databasehelper;

import com.thepepeyt.databasehelper.BaseTypes.Mysql;
import com.thepepeyt.databasehelper.BaseTypes.Sqlite3;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;


public class databasehelper {



    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        File file = new File("database.db");
        final Sqlite3 db = databasehelper.Sqlite3builder()
                .file(file)
                .build();

        db.connect();
        System.out.println("polaczono z baza danych");

        ArrayList<String> string = new ArrayList<>();
        string.add("String UUID");
        string.add("INT money");

        db.createTable("player", string);
        System.out.println("Stworzono player table");



        ArrayList<String> into = new ArrayList<>();
        string.add("UUID");
        string.add("money");

        ArrayList<Object> values = new ArrayList<>();
        values.add("234235252");
        values.add(100);

        db.insertInto("player", into, values);


        ArrayList<String> what = new ArrayList<>();
        string.add("UUID");


        Object money = db.getColumn("player", "money", what);
        System.out.println(money);

    }

        //MADE BY .XMON<3


        //MYSQLBUILDER

        public static MysqlBuilder Mysqlbuilder () {
            return new MysqlBuilder();


        }

        public static Sqlite3Builder Sqlite3builder () {
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
            if(!this.file.exists() & this.file == null){
                throw new IllegalStateException("Database file cannot be empty");
            }
            return new Sqlite3(file);
        }
    }
    }












