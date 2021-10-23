# DatabaseHelper
A Java library for easier using databases

# What DataBase is using library now?

Now in plans is only MySQL,SQLite3 and PostgreSQL

# How to use?
```java
    public void mysqltest() throws SQLException {
        Mysql db = databasehelper.Mysqlbuilder()
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

    }
        
```
