package com.kvest.bugtest.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import com.kvest.bugtest.R;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
