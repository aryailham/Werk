package com.example.werk;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String DatabaseName = "werkDb.db";
    private static final int DatabaseVersion = 2;

    public DatabaseOpenHelper(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
//        setForcedUpgrade(DatabaseVersion); //nanti cek2 lagi disini
    }
}

