package com.example.javier.midiabetes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelperMiDiabetes extends SQLiteOpenHelper{

    private static final String  DB_NAME = "MiDiabetesSQLite.sqlite";
    private static final int DB_VERSION = 1;

    public DbHelperMiDiabetes(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseManagerGlucosa.CREATE_TABLE);
        db.execSQL(DataBaseManagerDatosUsuario.CREATE_TABLE);
        db.execSQL(DataBaseManagerNutricion.CREATE_TABLE);
        db.execSQL(DataBaseManagerHemoglobina.CREATE_TABLE);
        db.execSQL(DataBaseManagerUsuarios.CREATE_TABLE);
        db.execSQL(DataBaseManagerComidas.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
