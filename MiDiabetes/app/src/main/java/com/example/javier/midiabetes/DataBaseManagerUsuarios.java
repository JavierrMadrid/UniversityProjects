package com.example.javier.midiabetes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManagerUsuarios {
    public static final String TABLE_NAME = "tableUsuarios";
    public static final String CN_ID = "_id";
    public static final String CN_NOMBRE = "nombre";
    public static final String CN_EMAIL = "email";
    public static final String CN_PASS= "password";

    private DbHelperMiDiabetes helper;
    private SQLiteDatabase db;

    public DataBaseManagerUsuarios(Context context) {
        helper = new DbHelperMiDiabetes(context);
        db = helper.getReadableDatabase();
    }

    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_NOMBRE + " text not null,"
            + CN_EMAIL + " text not null,"
            + CN_PASS + " text not null);";

    public Cursor cargarCursorUsuarios(){
        String[] columnas = new String[]{CN_ID,CN_NOMBRE,CN_EMAIL,CN_PASS};
        return db.query(TABLE_NAME, columnas, null,null,null,null,null);
    }

    public void insertar (String nombre, String email, String password){
        db.insert(TABLE_NAME, null, generarContentValues(nombre, email, password));
    }

    private ContentValues generarContentValues(String nombre, String email, String password) {
        ContentValues valores = new ContentValues();
        valores.put(CN_NOMBRE, nombre);
        valores.put(CN_EMAIL, email);
        valores.put(CN_PASS, password);
        return valores;
    }
}
