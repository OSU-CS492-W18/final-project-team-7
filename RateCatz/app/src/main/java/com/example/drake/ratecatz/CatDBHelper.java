package com.example.drake.ratecatz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aleaweeks on 3/16/18.
 */

public class CatDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cat.db";
    private static int DATABASE_VERSION = 1;

    public CatDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITED_CATS_TABLE =
                "CREATE TABLE " + CatContract.FavoritedCats.TABLE_NAME + "(" +
                        CatContract.FavoritedCats.COLUMN_CAT_URL + " TEXT NOT NULL, " +
                        CatContract.FavoritedCats.COLUMN_CAT_ID + " TEXT NOT NULL, " +
                        CatContract.FavoritedCats.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ");";
        db.execSQL(SQL_CREATE_FAVORITED_CATS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CatContract.FavoritedCats.TABLE_NAME + ";");
        onCreate(db);
    }
}
