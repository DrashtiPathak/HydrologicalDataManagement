package com.example.drashtimuni.hydrologicaldatamanagement;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * @author drashtimuni
 */
public class DatabaseOpenHelper extends SQLiteAssetHelper {

    public static final String DATABASE_NAME = "weather.db";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
}