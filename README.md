# DatabaseHelper
A Java library for easier using databases

# What Database is using library now?

Now in plans is only MySQL,SQLite3 and PostgreSQL
<br>
If u wanna me to add another database?
</br>
Join discord [DISCORD](https://discord.gg/94hn6qpj)


# Installation

Get it on [JitPack](https://jitpack.io/#ThePepeYT/databasehelper/-SNAPSHOT)

# How to use with Sqlite3?
```java
File file = new File("database.db");
SQLite3 db = DatabaseHelper.sqLite3Builder()
  .file(file)
  .build();
```
# How to use with MySQL?
```java
AbstractSQLDatabase db = DatabaseHelper.mySQLBuilder()
  .database("mydatabase")
  .host("myhost")
  //if you not gonna add port value default will be "3306"
  .port(3306)
  //if you not gonna add user default will be "root"
  .user("myuser")")
  .password("mypassword")
  .build();
```

# How to use with PostGreSQL?
```java
AbstractSQLDatabase db = DatabaseHelper.postgreSQLBuilder()
  .database("mydatabase")
  .host("myhost")
  //if you not gonna add port value default will be "3306"
  .port(3306)
  //if you not gonna add user default will be "root"
  .user("myuser")
  .password("mypassword")
  .build();
```

# How to use with MariaDB?
```java
AbstractSQLDatabase db = DatabaseHelper.mariaDBBuilder()
  .database("mydatabase")
  .host("myhost")
  //if you not gonna add port value default will be "3306"
  .port(3306)
   //if you not gonna add user default will be "root"
  .user("myuser")
  .password("mypassword")
  .build();
```


# Methods

## Connect
Connect method just create connection of your program with database.
<strong>U MUST USE IT BEFORE USING ANY METHOD</strong>

<br>

Example:
</br>
```java
File file = new File("database.db");
SQLite3 db = DatabaseHelper.sqLite3Builder()
  .file(file)
  .build();
 
 db.connect();

```
## CreateTable
Create table if not exists bruh
<br>
Example:
</br>
```java
File file = new File("database.db");
SQLite3 db = DatabaseHelper.sqLite3Builder()
   .file(file)
   .build();

ArrayList<String> string = new ArrayList<>();

string.add("UUID STRING");
string.add("LEVEL INT");
string.add("MONEY DOUBLE");

db.createTable("player", string);

//Create table "player" with columns
//UUID with String
//LEVEL with INT
//MONEY with DOUBLE
```

## insertInto
Insert data into table
<br>
<strong>To use it first u must create Table</strong>
Example:
</br>
```java
File file = new File("database.db");
SQLite3 db = DatabaseHelper.sqLite3Builder()
   .file(file)
   .build();


ArrayList<String> into = new ArrayList<>();
into.add("UUID");
into.add("LEVEL");
into.add("MONEY");

ArrayList<Object> values = new ArrayList<>();

values.add("2323234324");
values.add(2);
values.add(100.0);

db.insertInto("player", into, values);

//We inserted data to 3 columns
//Into UUID we inserted "2323234324"
//Into LEVEL we inserted 2
//Into MONEY we inserted 100.0

```

## getColumn
Get data from choosed column
<br>
Example:
</br>

```java
File file = new File("database.db");
SQLite3 db = DatabaseHelper.sqLite3Builder()
  .file(file)
  .build();
  
  
ArrayList<String> what = new ArrayList<>();
what.add("UUID");

ArrayList<Object> where = new ArrayList<>();
where.add("2323234324");


db.getColumn("player", "MONEY", what, where).thenAccept(System.out::println);
//This code gonna return 100.0 that we inserted in code above
```

## updateColumn
Update data from choosed column
<br>
Example:
</br>
```java
File file = new File("database.db");
SQLite3 db = DatabaseHelper.sqLite3Builder()
  .file(file)
  .build();
  
ArrayList<String> what = new ArrayList<>();
what.add("UUID");

ArrayList<Object> where = new ArrayList<>();
where.add("2323234324");

db.updateColumn("player", "MONEY", what, where, 150);

//Updates column from table player where column UUID is "2323234324"
```

## ifExists
Check if in table is column with an data

```java
File file = new File("database.db");
SQLite3 db = DatabaseHelper.sqLite3Builder()
  .file(file)
  .build();
ArrayList<String> what = new ArrayList<>();
what.add("UUID");

ArrayList<Object> where = new ArrayList<>();
where.add("2323234324");

db.ifExists("player", what, where).thenAccept(System.out::println);

//check if in table "player" is column with UUID "2323234324"



```

## disconnect
Disconnect method just disonnects your program from database
```java
File file = new File("database.db");
SQLite3 db = DatabaseHelper.sqLite3Builder()
  .file(file)
  .build();
  
db.disconnect();
```
