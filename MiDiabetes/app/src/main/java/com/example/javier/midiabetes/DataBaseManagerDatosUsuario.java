package com.example.javier.midiabetes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManagerDatosUsuario {
    public static final String TABLE_NAME = "tablaDatosUsuario";
    public static final String CN_ID = "_id";
    public static final String CN_NOMBRE = "nombre";
    public static final String CN_PESO= "peso";
    public static final String CN_EDAD = "edad";
    public static final String CN_MG_AYUNAS = "maxGlucosaAyunas";
    public static final String CN_MG_COMIDO = "maxGlucosaComido";
    public static final String CN_MG_NOCHE = "maxGlucosaNoche";
    public static final String CN_mG_AYUNAS = "minGlucosaAyunas";
    public static final String CN_mG_COMIDO = "minGlucosaComido";
    public static final String CN_mG_NOCHE = "minGlucosaNoche";
    public static final String CN_HbA1c = "hba1c";

    private DbHelperMiDiabetes helper;
    private SQLiteDatabase db;

    public DataBaseManagerDatosUsuario(Context context) {
        helper = new DbHelperMiDiabetes(context);
        db = helper.getReadableDatabase();
    }

    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_NOMBRE + " text not null,"
            + CN_PESO + " text,"
            + CN_EDAD + " text,"
            + CN_MG_AYUNAS + " text,"
            + CN_MG_COMIDO + " text,"
            + CN_MG_NOCHE + " text,"
            + CN_mG_AYUNAS + " text,"
            + CN_mG_COMIDO + " text,"
            + CN_mG_NOCHE + " text,"
            + CN_HbA1c + " text);";

    public Cursor cargarCursorUsuario(){
        String[] columnas = new String[]{CN_ID,CN_NOMBRE,CN_PESO,CN_EDAD,CN_MG_AYUNAS,CN_MG_COMIDO,CN_MG_NOCHE,CN_mG_AYUNAS,CN_mG_COMIDO,CN_mG_NOCHE,CN_HbA1c};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);
    }

    public void insertar (String nombre, String peso, String edad, String maxGlucosaAyunas, String maxGlucosaComido, String maxGlucosNoche,
                          String minGlucosaAyunas, String minglucosaComido, String minGlucosaNoche, String hba1c){
        db.insert(TABLE_NAME, null, generarContentValues(nombre, peso, edad, maxGlucosaAyunas, maxGlucosaComido, maxGlucosNoche, minGlucosaAyunas, minglucosaComido,
                minGlucosaNoche, hba1c));
    }

    public void modificar(String nombre, String peso, String edad, String maxGlucosaAyunas, String maxGlucosaComido, String maxGlucosNoche,
                          String minGlucosaAyunas, String minglucosaComido, String minGlucosaNoche, String hba1c) {
        db.update(TABLE_NAME, generarContentValues(nombre, peso, edad, maxGlucosaAyunas, maxGlucosaComido, maxGlucosNoche, minGlucosaAyunas, minglucosaComido,
                minGlucosaNoche, hba1c), CN_NOMBRE + "=?", new String[]{nombre});
    }

    public void eliminar(String id){
        db.delete(TABLE_NAME, CN_ID + "=?", new String[]{id});
    }

    private ContentValues generarContentValues(String nombre, String peso, String edad, String maxGlucosaAyunas, String maxGlucosaComido, String maxGlucosNoche,
                                               String minGlucosaAyunas, String minglucosaComido, String minGlucosaNoche, String hba1c) {
        ContentValues valores = new ContentValues();
        valores.put(CN_NOMBRE, nombre);
        valores.put(CN_PESO, peso);
        valores.put(CN_EDAD, edad);
        valores.put(CN_MG_AYUNAS, maxGlucosaAyunas);
        valores.put(CN_MG_COMIDO, maxGlucosaComido);
        valores.put(CN_MG_NOCHE, maxGlucosNoche);
        valores.put(CN_mG_AYUNAS, minGlucosaAyunas);
        valores.put(CN_mG_COMIDO, minglucosaComido);
        valores.put(CN_mG_NOCHE, minGlucosaNoche);
        valores.put(CN_HbA1c, hba1c);

        return valores;
    }
}

