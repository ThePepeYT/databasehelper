# DatabaseHelper
A Java library for easier using databases

# What Database is using library now?

Now in plans is only MySQL,SQLite3 and PostgreSQL

# Installation

Get it on [JitPack](https://jitpack.io/#ThePepeYT/databasehelper/-SNAPSHOT)

# How to use with Sqlite3?
```java
        File file = new File("database.db");
        final Sqlite3 db = databasehelper.Sqlite3builder()
                .file(file)
                .build();


    
        
```
# How to use with MySQL?
```java
        final Mysql db = new MysqlBuilder()
                .database("mydatabase")
                .host("myhost")
                .port(3306)
                .user("myuser")
                .password("mypassword")
                .build();

        
```

# How to use with PostGreSQL?
```java
        final Postgresql db = new PostgresqlBuilder()
                .database("mydatabase")
                .host("myhost")
                .port(3306)
                .user("myuser")
                .password("mypassword")
                .build();

        
```


# Methods
```java
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
```

