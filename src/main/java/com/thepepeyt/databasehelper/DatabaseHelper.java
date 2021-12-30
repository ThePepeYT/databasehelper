package com.thepepeyt.databasehelper;

import com.thepepeyt.databasehelper.database.AbstractSQLDatabase;
import com.thepepeyt.databasehelper.database.type.*;


import java.io.File;
import java.sql.SQLException;


public class DatabaseHelper {





    public static MySQLBuilder mySQLBuilder() {
        return new MySQLBuilder();
    }



    public static MariaDBBuilder mariaDBBuilder() {
        return new MariaDBBuilder();
    }

    public static SQLite3Builder sqLite3Builder() {
        return new SQLite3Builder();
    }

    public static PostgresqlBuilder postgreSQLBuilder() {
        return new PostgresqlBuilder();
    }

    public static abstract class SQLBuilder<T extends AbstractSQLDatabase> {
        private final DatabaseType type;
        private String host;
        private int port = 3306;
        private String database;
        private String user = "root";
        private String password;

        public SQLBuilder(final DatabaseType type) {
            this.type = type;
        }

        public SQLBuilder<T> host(final String host) {
            this.host = host;
            return this;
        }

        public SQLBuilder<T> port(final int port) {
            this.port = port;
            return this;
        }

        public SQLBuilder<T> database(final String database) {
            this.database = database;
            return this;
        }

        public SQLBuilder<T> user(final String user) {
            this.user = user;
            return this;
        }

        public SQLBuilder<T> password(final String password) {
            this.password = password;
            return this;
        }

        public AbstractSQLDatabase build() {
            if (this.host.isEmpty()) {
                throw new IllegalStateException("Host name cannot be empty");
            }
            if (this.database.isEmpty()) {
                throw new IllegalStateException("Database name cannot be empty");
            }
            if (password.isEmpty()) {
                throw new IllegalStateException("Password cannot be empty");
            }
            switch (type) {
                case MYSQL:
                    return new MySQL(database, host, user, password, port);
                case POSTGRESQL:
                    return new PostgreSQL(database, host, user, password, port);
                default:
                    throw new IllegalStateException("Database " + type.name() + " is unsupported");
            }
        }
    }

    public static final class SQLite3Builder {
        private File file;

        public SQLite3Builder file(final File database) {
            this.file = database;
            return this;
        }

        public SQLite3 build() {
            if (!this.file.exists() & this.file == null) {
                throw new IllegalStateException("Database file cannot be empty");
            }
            return new SQLite3(file);
        }
    }



    public static final class MySQLBuilder extends SQLBuilder<MySQL> {
        public MySQLBuilder() {
            super(DatabaseType.MYSQL);
        }
    }
    public static final class PostgresqlBuilder extends SQLBuilder<PostgreSQL> {
        public PostgresqlBuilder() {
            super(DatabaseType.POSTGRESQL);
        }
    }

    public static final class MariaDBBuilder extends SQLBuilder<PostgreSQL> {
        public MariaDBBuilder() {
            super(DatabaseType.MARIADB);
        }
    }


}

















