package com.kvest.bugtest.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.kvest.bugtest.R;
import com.kvest.bugtest.datastorage.table.TestTable;

/**
 * Created with IntelliJ IDEA.
 * User: Kvest
 * Date: 28.12.13
 * Time: 20:50
 * To change this template use File | Settings | File Templates.
 */
public class TestListAdapter extends SimpleCursorAdapter implements SimpleCursorAdapter.ViewBinder {
    private CompoundButton.OnCheckedChangeListener checkedListener;
    private OnFavoriteStateChangedListener onFavoriteStateChangedListener;

    public TestListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        setViewBinder(this);

        checkedListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                long id = ((Long)buttonView.getTag()).longValue();

                Log.d("KVEST_TAG", id + "=[" + isChecked + "]");

                if (onFavoriteStateChangedListener != null) {
                    onFavoriteStateChangedListener.onFavoriteStateChanged(id, isChecked);
                }
            }
        };
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = super.newView(context, cursor, parent);

        //set listener
        ((CheckBox)view.findViewById(R.id.favorite)).setOnCheckedChangeListener(checkedListener);

        return view;
    }

    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        if (view.getId() == R.id.favorite && columnIndex == cursor.getColumnIndex(TestTable._ID)) {
            view.setTag(new Long(cursor.getLong(columnIndex)));
            return true;
        } else if (view.getId() == R.id.favorite && columnIndex == cursor.getColumnIndex(TestTable.FAVORITE_COLUMN)) {
            ((CheckBox) view).setChecked(cursor.getInt(columnIndex) != 0);
            return true;
        }
        return false;
    }

    public void setOnFavoriteStateChangedListener(OnFavoriteStateChangedListener onFavoriteStateChangedListener) {
        this.onFavoriteStateChangedListener = onFavoriteStateChangedListener;
    }

    public interface  OnFavoriteStateChangedListener {
        public void onFavoriteStateChanged(long id, boolean value);
    }
}
