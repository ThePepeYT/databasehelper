package com.thepepeyt.databasehelper.database;

import java.io.File;

public abstract class AbstractLiteDatabase extends SQLStatement {
    protected final File file;

    protected AbstractLiteDatabase(final File file) {
        this.file = file;
    }

    protected File getFile() {
        return file;
    }
}
