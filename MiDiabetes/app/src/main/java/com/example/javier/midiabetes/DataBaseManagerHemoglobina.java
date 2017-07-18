package com.example.javier.midiabetes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManagerHemoglobina {
    public static final String TABLE_NAME = "tablaHemoglobina";
    public static final String CN_ID = "_id";
    public static final String CN_HbA1c = "hba1c";
    public static final String CN_FECHA = "fecha";

    private SQLiteDatabase db;

    public DataBaseManagerHemoglobina(Context context) {
        DbHelperMiDiabetes helper = new DbHelperMiDiabetes(context);
        db = helper.getReadableDatabase();
    }

    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_HbA1c + " text,"
            + CN_FECHA + " text);";

    public Cursor cargarCursorHemoglobina(){
        String[] columnas = new String[]{CN_ID,CN_HbA1c,CN_FECHA};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);
    }

    public void insertar (String hba1c, String fecha){
        db.insert(TABLE_NAME, null, generarContentValues(hba1c, fecha));
    }

    public void eliminar(String id){
        db.delete(TABLE_NAME, CN_ID + "=?", new String[]{id});
    }

    private ContentValues generarContentValues(String hba1c, String fecha) {
        ContentValues valores = new ContentValues();
        valores.put(CN_HbA1c, hba1c);
        valores.put(CN_FECHA, fecha);

        return valores;
    }
}
