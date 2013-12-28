package com.kvest.bugtest.contentprovide;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import com.kvest.bugtest.datastorage.TestSQLStorage;
import com.kvest.bugtest.datastorage.table.TestTable;

/**
 * Created with IntelliJ IDEA.
 * User: Kvest
 * Date: 28.12.13
 * Time: 20:41
 * To change this template use File | Settings | File Templates.
 */
public class TestProvider extends ContentProvider {
    private TestSQLStorage sqlStorage;

    private static final int ITEMS_URI_INDICATOR = 1;
    private static final int ITEM_URI_INDICATOR = 2;

    private static final UriMatcher uriMatcher;
    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(TestProviderMetadata.AUTHORITY, TestProviderMetadata.ITEMS_PATH, ITEMS_URI_INDICATOR);
        uriMatcher.addURI(TestProviderMetadata.AUTHORITY, TestProviderMetadata.ITEMS_PATH + "/#", ITEM_URI_INDICATOR);
    }

    @Override
    public boolean onCreate() {
        sqlStorage = new TestSQLStorage(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch(uriMatcher.match(uri))
        {
            case ITEMS_URI_INDICATOR :
                queryBuilder.setTables(TestTable.TABLE_NAME);
                break;
            case ITEM_URI_INDICATOR :
                queryBuilder.setTables(TestTable.TABLE_NAME);
                queryBuilder.appendWhere(TestTable._ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown uri = " + uri);
        }

        //make query
        SQLiteDatabase db = sqlStorage.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        // Make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = sqlStorage.getWritableDatabase();
        long rowId = 0;

        switch (uriMatcher.match(uri)) {
            case ITEMS_URI_INDICATOR:
                rowId = db.insert(TestTable.TABLE_NAME, null, values);
                if (rowId > 0)
                {
                    Uri resultUri = ContentUris.withAppendedId(uri, rowId);
                    getContext().getContentResolver().notifyChange(resultUri, null);
                    return resultUri;
                }
                break;
        }

        throw new IllegalArgumentException("Faild to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionParams) {
        int rowsUpdated = 0;
        boolean hasSelection = !TextUtils.isEmpty(selection);
        SQLiteDatabase db = sqlStorage.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case ITEMS_URI_INDICATOR :
                rowsUpdated = db.update(TestTable.TABLE_NAME, values, selection, selectionParams);
                break;
            case ITEM_URI_INDICATOR :
                rowsUpdated = db.update(TestTable.TABLE_NAME, values, TestTable._ID + "=" + uri.getLastPathSegment() +
                        (hasSelection ? (" AND " + selection) : ""), (hasSelection ? selectionParams : null));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (rowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
