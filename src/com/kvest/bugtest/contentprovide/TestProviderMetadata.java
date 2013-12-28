package com.kvest.bugtest.contentprovide;

import android.net.Uri;

/**
 * Created with IntelliJ IDEA.
 * User: Kvest
 * Date: 28.12.13
 * Time: 20:41
 * To change this template use File | Settings | File Templates.
 */
public abstract class TestProviderMetadata {
    public static final String AUTHORITY = "com.kvest.bugtest.contentprovide.TestProvider";

    public static final String ITEMS_PATH = "items";

    public static final Uri ITEMS_URI = Uri.parse("content://" + AUTHORITY + "/" + ITEMS_PATH);
}
