package com.example.javier.midiabetes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManagerNutricion {
    public static final String TABLE_NAME = "tablaNutrional";
    public static final String CN_ID = "_id";
    public static final String CN_COMIDA = "comida";
    public static final String CN_PESO = "peso";
    public static final String CN_HIDRATOSXRACION= "hidratosxracion";
    public static final String CN_RACIONES = "raciones";
    public static final String CN_INGREDIENTES = "ingredientes";
    public static final String CN_FECHA = "fecha";

    private DbHelperMiDiabetes helper;
    private SQLiteDatabase db;

    public DataBaseManagerNutricion(Context context) {
        helper = new DbHelperMiDiabetes(context);
        db = helper.getReadableDatabase();
    }

    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_COMIDA + " text not null,"
            + CN_PESO + " text,"
            + CN_HIDRATOSXRACION + " text,"
            + CN_RACIONES + " text,"
            + CN_INGREDIENTES + " text,"
            + CN_FECHA + " text);";

    public Cursor cargarCursorNutricion(){
        String[] columnas = new String[]{CN_ID,CN_COMIDA,CN_PESO,CN_HIDRATOSXRACION,CN_RACIONES,CN_INGREDIENTES,CN_FECHA};
        return db.query(TABLE_NAME, columnas, null,null,null,null,null);
    }

    public void insertar (String comida, String peso, String hidratosxracion, String raciones, String ingredientes, String fecha){
        db.insert(TABLE_NAME, null, generarContentValues(comida, peso, hidratosxracion, raciones, ingredientes, fecha));
    }

    private ContentValues generarContentValues(String comida, String peso, String hidratosxracion, String raciones, String ingredientes, String fecha) {
        ContentValues valores = new ContentValues();
        valores.put(CN_COMIDA, comida);
        valores.put(CN_PESO, peso);
        valores.put(CN_HIDRATOSXRACION, hidratosxracion);
        valores.put(CN_RACIONES, raciones);
        valores.put(CN_INGREDIENTES, ingredientes);
        valores.put(CN_FECHA, fecha);
        return valores;
    }
}

