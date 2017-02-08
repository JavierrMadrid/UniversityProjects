package com.example.javier.midiabetes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Javier on 08/12/2016.
 */

public class DbHelperAlimentos extends SQLiteOpenHelper{

    private static final String  DB_NAME = "TablaHCAlimentosSQLite.sqlite";
    private static final int DB_VERSION = 1;

    public DbHelperAlimentos(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseManagerAlimentos.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
