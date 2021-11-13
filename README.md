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

# How to use with Redis?
```java
Redis db = DatabaseHelper.redisBuilder()
   .host("localhost")
   .timeout(600000000)
   .port(6379)
   .password("")
   .build();
```


# Methods SQL
Methods that u use with SQL based db

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

db.createTable("player", List.of("NAME STRING", "MONEY DOUBLE", "LEVEL INT"));

//Create table "player" with columns
//NAME with String
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


db.insertInto("player", List.of("NAME", "MONEY", "LEVEL"), List.of("ThePepeYT", 100.0, 1));
db.insertInto("player", List.of("NAME", "MONEY", "LEVEL"), List.of("Topfu", 150.0, 2));
db.insertInto("player", List.of("NAME", "MONEY", "LEVEL"), List.of("Xmon", 200.0, 3));

//We inserted new informations to as columns
//Into UUID we inserted ThePepeYT, Topfu and Xmon
//Into LEVEL we inserted 1,2,3
//Into MONEY we inserted 100.0,150.0,200.0

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
  
db.getColumn("player", "MONEY", List.of("NAME"), List.of("Xmon"))
  .thenAccept(object -> {
     if(object.isPresent()) {
      System.out.println(object.get());
  }
});

db.getColumn("player", "MONEY", List.of("NAME"), List.of("ThePepeYT"))
  .thenAccept(object -> {
     if(object.isPresent()) {
      System.out.println(object.get());
  }
});

db.getColumn("player", "MONEY", List.of("NAME"), List.of("Topfu"))
  .thenAccept(object -> {
     if(object.isPresent()) {
      System.out.println(object.get());
  }
});



//This code gonna return 200.0, 100.0, 150.0 that we inserted in code above
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
  
db.updateColumn("player", "MONEY", List.of("NAME"), List.of("Topfu"), 300.0);

//Updates column from table player where column NAME is "Topfu" to 300.0
```

## ifExists
Check if in table is column with an data

```java
File file = new File("database.db");
SQLite3 db = DatabaseHelper.sqLite3Builder()
  .file(file)
  .build();

db.ifExists("player", List.of("NAME"), List.of("Xmon"));

//check if in table "player" is column with NAME "Xmon"
//and its gonna return true :)

```

## getLeadboard
Return list of lists where inside list are values
<br>
Example:
</br>
```java
File file = new File("database.db");
SQLite3 db = DatabaseHelper.sqLite3Builder()
  .file(file)
  .build();






db.getLeadboard("player", 3, "MONEY", List.of("NAME", "LEVEL"))
  .thenAccept(object -> {
    if(object.isPresent()){
      System.out.println(object.get());
     }
});
//It's gonna return [[Xmon, 3], [Topfu, 2], [ThePepeYT, 1]
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

# Methods Redis
methods that u use with redis

## connect
Connect your programm with database
```java
Redis db = DatabaseHelper.redisBuilder()
   .host("localhost")
   .timeout(600000000)
   .port(6379)
   .password("")
   .build();
db.connect();
```

## setData
Sets data to key-value dictionary
```java
Redis db = DatabaseHelper.redisBuilder()
   .host("localhost")
   .timeout(600000000)
   .port(6379)
   .password("")
   .build();
redis.setData(List.of("thepepeyt"), List.of("100"));
redis.setData(List.of("xmon"), List.of("200"));
redis.setData(List.of("topfu"), List.of("300"));
//Creates 3 keys named thepepeyt, xmon, topfu with values 100, 200, 300
//ADVANCED VERSION
redis.setData(ADVxmon, List.of("100", UUID.randomUUID().toString()));
redis.setData(ADVthepepeyt, List.of("200", UUID.randomUUID().toString()));
redis.setData(ADVtopfu, List.of("300", UUID.randomUUID().toString()));
//Creates 3 keys named thepepeyt, xmon, topfu with list of values
```

