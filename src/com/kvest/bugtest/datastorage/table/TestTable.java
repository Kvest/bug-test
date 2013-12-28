package com.kvest.bugtest.datastorage.table;

import android.provider.BaseColumns;

/**
 * Created with IntelliJ IDEA.
 * User: Kvest
 * Date: 28.12.13
 * Time: 20:25
 * To change this template use File | Settings | File Templates.
 */
public abstract class TestTable implements BaseColumns {
    public static final String TABLE_NAME = "test";

    public static final String NAME_COLUMN = "name";
    public static final String FAVORITE_COLUMN = "favorite";

    public static final String[] FULL_PROJECTION = {_ID, NAME_COLUMN, FAVORITE_COLUMN};

    public static final String CREATE_TABLE_SQL = "CREATE TABLE \"" + TABLE_NAME + "\" (\"" +
            _ID + "\" INTEGER PRIMARY KEY AUTOINCREMENT, \"" +
            NAME_COLUMN + "\" TEXT DEFAULT \"\", \"" +
            FAVORITE_COLUMN + "\" INTEGER DEFAULT 0);";

    public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS \"" + TABLE_NAME + "\";";
}
