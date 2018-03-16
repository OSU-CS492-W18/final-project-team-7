package com.example.drake.ratecatz;

import android.provider.BaseColumns;

/**
 * Created by aleaweeks on 3/16/18.
 */

public class CatContract {
    private CatContract() {}
    public static class FavoritedCats implements BaseColumns {
            public static final String TABLE_NAME = "favoritedCats";
            public static final String COLUMN_CAT_URL = "url";
            public static final String COLUMN_CAT_ID = "id";
            public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
