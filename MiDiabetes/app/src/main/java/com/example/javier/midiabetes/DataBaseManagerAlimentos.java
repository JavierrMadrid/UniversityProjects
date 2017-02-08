package com.example.javier.midiabetes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Javier on 27/11/2015.
 */
public class DataBaseManagerAlimentos {
    public static final String TABLE_NAME = "tablaHCAlimentos";
    public static final String CN_ID = "_id";
    public static final String CN_AlIMENTO = "alimento";
    public static final String CN_GRAMOSRACION= "gramosXracion";
    public static final String CN_CONSUMO = "consumo";
    public static final String CN_RACIONESXCONSUMO = "racionesXconsumo";
    public static final String CN_IG = "ig";

    private DbHelperAlimentos helper;
    private SQLiteDatabase db;

    public DataBaseManagerAlimentos(Context context) {
        helper = new DbHelperAlimentos(context);
        db = helper.getReadableDatabase();
    }

    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_AlIMENTO + " text not null,"
            + CN_GRAMOSRACION + " text,"
            + CN_CONSUMO + " text,"
            + CN_RACIONESXCONSUMO + " text,"
            + CN_IG + " text not null);";

    public Cursor cargarCursorAlimentos(){
        String[] columnas = new String[]{CN_ID,CN_AlIMENTO,CN_GRAMOSRACION,CN_CONSUMO,CN_RACIONESXCONSUMO,CN_IG};
        return db.query(TABLE_NAME, columnas, null,null,null,null,null);
    }
    public Cursor buscarAlimentos(String comida){
        String[] columnas = new String[]{CN_ID,CN_AlIMENTO,CN_GRAMOSRACION,CN_CONSUMO,CN_RACIONESXCONSUMO,CN_IG};
        return db.query(TABLE_NAME,columnas,CN_AlIMENTO + "=?",new String[]{comida},null,null,null);
    }
}

