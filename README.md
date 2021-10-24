# DatabaseHelper
A Java library for easier using databases

# What DataBase is using library now?

Now in plans is only MySQL,SQLite3 and PostgreSQL

# How to use?
```java
        File file = new File("database.db");
        final Sqlite3 db = databasehelper.Sqlite3builder()
                .file(file)
                .build();

        db.connect();
        System.out.println("polaczono z baza danych");

        ArrayList<String> string = new ArrayList<>();
        string.add("UUID STRING");
        string.add("LEVEL INT");
        string.add("MONEY Int");

        db.createTable("player", string);
        System.out.println("Stworzono player table");


        ArrayList<String> into = new ArrayList<>();
        into.add("UUID");
        into.add("LEVEL");
        into.add("MONEY");

        ArrayList<Object> values = new ArrayList<>();
        values.add("2323234324");
        values.add(2);
        values.add(100);


        db.insertInto("player",into,values);
        System.out.println("Zinsertowano");


        ArrayList<String> what = new ArrayList<>();
        values.add("UUID");

        ArrayList<Object> where = new ArrayList<>();
        values.add("2323234324");




        System.out.println(db.getColumn("player", "MONEY", what, where));

        db.updateColumn("player", "MONEY", what, where, 150);

        System.out.println(db.getColumn("player", "MONEY", what, where));


    }
        
```
