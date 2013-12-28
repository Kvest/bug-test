package com.kvest.bugtest.datastorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.kvest.bugtest.datastorage.table.TestTable;

/**
 * Created with IntelliJ IDEA.
 * User: Kvest
 * Date: 28.12.13
 * Time: 20:27
 * To change this template use File | Settings | File Templates.
 */
public class TestSQLStorage extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "test_db";
    private static final int DATABASE_VERSION = 1;

    public TestSQLStorage(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create tables
        db.execSQL(TestTable.CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Nothing to do
    }
}
