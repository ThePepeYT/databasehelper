package com.thepepeyt.databasehelper.Utils;


public class DatabaseExceptions extends Exception {

    String message;

    public DatabaseExceptions(String str) {
        message = str;
    }

    public String toString() {
        return ("[ERROR] DatabaseHelper: " + message);
    }
}



