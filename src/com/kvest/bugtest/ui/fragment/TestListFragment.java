package com.kvest.bugtest.ui.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import com.kvest.bugtest.R;
import com.kvest.bugtest.contentprovide.TestProviderMetadata;
import com.kvest.bugtest.datastorage.table.TestTable;
import com.kvest.bugtest.ui.adapter.TestListAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Kvest
 * Date: 28.12.13
 * Time: 20:49
 * To change this template use File | Settings | File Templates.
 */
public class TestListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>,
                                                              TestListAdapter.OnFavoriteStateChangedListener {
    private static final String[] PROJECTION = {TestTable._ID, TestTable.NAME_COLUMN, TestTable.FAVORITE_COLUMN };
    private static final int LOAD_ITEMS_ID = 0;

    private TestListAdapter adapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //create and set adapter
        String[] from = {TestTable._ID, TestTable.NAME_COLUMN, TestTable.FAVORITE_COLUMN};
        int[] to = {R.id.favorite, R.id.name, R.id.favorite};
        adapter = new TestListAdapter(getActivity(), R.layout.test_list_item, null, from, to, TestListAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        adapter.setOnFavoriteStateChangedListener(this);
        setListAdapter(adapter);

        //load cursor
        getActivity().getSupportLoaderManager().initLoader(LOAD_ITEMS_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        switch (id) {
            case LOAD_ITEMS_ID : return new CursorLoader(getActivity(), TestProviderMetadata.ITEMS_URI,
                                                         PROJECTION, null, null, null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);

        //add new items if list is empty
        if (cursor.getCount() == 0) {
            createItems();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onFavoriteStateChanged(long id, boolean value) {
        ContentValues values = new ContentValues(1);
        values.put(TestTable.FAVORITE_COLUMN, (value ? 1 : 0));
        getActivity().getContentResolver().update(Uri.withAppendedPath(TestProviderMetadata.ITEMS_URI, Long.toString(id)),
                                                  values, null, null);
    }

    private void createItems() {
        for (int i = 0; i < 50; ++i) {
            ContentValues values = new ContentValues(1);
            values.put(TestTable.NAME_COLUMN, "Item " + i);

            getActivity().getContentResolver().insert(TestProviderMetadata.ITEMS_URI, values);
        }
    }
}
