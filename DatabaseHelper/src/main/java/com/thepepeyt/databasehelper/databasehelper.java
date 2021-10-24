package com.thepepeyt.databasehelper;

import com.thepepeyt.databasehelper.BaseTypes.Mysql;

import java.sql.SQLException;
import java.util.ArrayList;


public class databasehelper {


    public static void main(String[] args) throws SQLException {
        final Mysql db = databasehelper.Mysqlbuilder()
                .database("mydatabase")
                .host("myhost")
                //if you not gonna add port to builder its gonna be 3306
                .password("mypassword")
                //if you not gonna add user to builder its gonna be root
                .user("myuser")
                .port(3306)
                .build();
        ArrayList<String> string = new ArrayList<>();
        string.add("String UUID");
        string.add("INT money");

        db.createTable("player", string);

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


        public static MysqlBuilder Mysqlbuilder () {
            return new MysqlBuilder();


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
    }












