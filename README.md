<div align="center">
    <a href="https://github.com/ThePepeYT/databasehelper/"><img src="images-removebg-preview.png" alt="databaseimg" height="128" style="border-radius: 50%"></a>
    <div>
        <h1><strongDatabaseHelper</strong></h1>
    </div>
</div>


# 👋 Hello!
<h3>DatabaseHelper is lightweight library that makes using databases a lot faster </h3>

# 🔩 Installation
### Get it on jitpack [DatabaseHelper](https://jitpack.io/#ThePepeYT/databasehelper)

# 📙Examples

### Connect
<h4>Creates connection beetwen your code and database</h4>

```java
SQLite3 db = DatabaseHelper.sqLite3Builder()
  .file(new File("database.db"))
  .build();
  
db.connect();
```

### Disconnect
<h4>Disconnects code from the database</h4>

```java
SQLite3 db = DatabaseHelper.sqLite3Builder()
  .file(new File("database.db"))
  .build();

db.disconnect();
```


## Rows

### insertData
<h4>Insert data to table</h4>

```java
SQLite3 db = new SQLite3(new File("database.db"));

db.connect();

//Inserts into "mycolumn" value "myvalue" in table "mytable"
db.insertData().table("mytable").insert("mycolumn", "myvalue").executeAsync();
```
### getData
<h4>Get data from table</h4>

```java
SQLite3 db = new SQLite3(new File("database.db"));

db.connect();

//Gets data "PLAYERNAME" and "PLAYERMONEY" where "PLAYERID" is "SOMEID" and print list of it
var data = db.getData().table("TABLE")
  .columns("PLAYERNAME", "PLAYERMONEY")
  .where("PLAYERID", "SOMEID").completeAsync();

data.subscribe(System.out::println);
```

### updateData
<h4>Update data from database</h4>

```java
SQLite3 db = new SQLite3(new File("database.db"));

db.connect();

//Updates "MYCOLUMN" by value "myvalue" in table "TABLE"
db.updateData().table("TABLE").column("MYCOLUMN", "myvalue").executeAsync();
```

### deleteData
<h4>Delete data from database</h4>

```
SQLite3 db = new SQLite3(new File("database.db"));

db.connect();
        
//Deletes every row from table "TABLE" where "ID" is 100
db.deleteData().table("TABLE").where("ID", 100).executeAsync();
```

## Tables

### createTable
<h4>Creates table as name says xD</h4>

```java
SQLite3 db = new SQLite3(new File("database.db"));

db.connect();
        
//Creates table with column "1COLUMN" that can store TEXT and "2COLUMN" that can store INT
db.createTable().table("TABLE").columns("1COLUMN TEXT", "2COLUMN INT").executeAsync();
```

## Columns

### deleteColumn
<h4>Delets columns from table</h4>

```java
SQLite3 db = new SQLite3(new File("database.db"));

db.connect();

//Deletes 2 columns from table "TABLE"
db.deleteColumn().table("TABLE").columns("1COLUMN", "2COLUMN").executeAsync();
```
### addColumn
<h4>Add columns to table</h4>

```java
SQLite3 db = new SQLite3(new File("database.db"));

db.connect();

//Add to table "TABLE" columns "3COLUMN" that can store text and add "4COLUMN" that can store 
db.addColumn().table("TABLE").columns("3COLUMN TEXT", "4COLUMN INT").executeAsync();
```

# 🚀 Others
### If you have any problems join this discord [Support](https://discord.gg/A4XZFze8WU) or you can contact me on Discord: ThePepeYT#1139.


## ❤️Thanks to:
[Xmonpl](https://github.com/xmonpl) for all help with Rxjava
<br>
[dan1st](https://github.com/danthe1st) for all help with small fixes</h4>
