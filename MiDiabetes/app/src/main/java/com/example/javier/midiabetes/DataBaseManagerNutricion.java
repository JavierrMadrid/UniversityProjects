package com.example.javier.midiabetes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManagerNutricion {
    public static final String TABLE_NAME = "tablaNutrional";
    public static final String CN_ID = "_id";
    public static final String CN_AlIMENTO = "alimento";
    public static final String CN_GRAMOSRACION= "gramosXracion";
    public static final String CN_CONSUMO = "consumo";
    public static final String CN_RACIONESXCONSUMO = "racionesXconsumo";
    public static final String CN_IG = "ig";
    public static final String CN_FECHA = "fecha";

    private DbHelperMiDiabetes helper;
    private SQLiteDatabase db;

    public DataBaseManagerNutricion(Context context) {
        helper = new DbHelperMiDiabetes(context);
        db = helper.getReadableDatabase();
    }

    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_AlIMENTO + " text not null,"
            + CN_GRAMOSRACION + " text,"
            + CN_CONSUMO + " text,"
            + CN_RACIONESXCONSUMO + " text,"
            + CN_IG + " text not null,"
            + CN_FECHA + "text not null);";

    public Cursor cargarCursorNutricion(){
        String[] columnas = new String[]{CN_ID,CN_AlIMENTO,CN_GRAMOSRACION,CN_CONSUMO,CN_RACIONESXCONSUMO,CN_IG,CN_FECHA};
        return db.query(TABLE_NAME, columnas, null,null,null,null,null);
    }

    public void insertar (String alimento, String gramosXracion, String consumo, String racionesXconsumo, String ig, String fecha){
        db.insert(TABLE_NAME, null, generarContentValues(alimento, gramosXracion, consumo, racionesXconsumo, ig, fecha));
    }

    private ContentValues generarContentValues(String alimento, String gramosXracion, String consumo, String racionesXconsumo, String ig, String fecha) {
        ContentValues valores = new ContentValues();
        valores.put(CN_AlIMENTO, alimento);
        valores.put(CN_GRAMOSRACION, gramosXracion);
        valores.put(CN_CONSUMO, consumo);
        valores.put(CN_RACIONESXCONSUMO, racionesXconsumo);
        valores.put(CN_IG, ig);
        valores.put(CN_FECHA, fecha);
        return valores;
    }
}

