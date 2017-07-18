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
    public static final String CN_DEBUT = "debut";
    public static final String CN_MG_AYUNAS = "maxGlucosaAyunas";
    public static final String CN_MG_COMIDO = "maxGlucosaComido";
    public static final String CN_MG_NOCHE = "maxGlucosaNoche";
    public static final String CN_mG_AYUNAS = "minGlucosaAyunas";
    public static final String CN_mG_COMIDO = "minGlucosaComido";
    public static final String CN_mG_NOCHE = "minGlucosaNoche";
    public static final String CN_mG_ESTANDAR = "minGlucosaEstandar";
    public static final String CN_MG_ESTANDAR = "maxGlucosaEstandar";
    public static final String CN_HbA1c = "hba1c";
    public static final String CN_IS = "indiceSensibilidad";

    private SQLiteDatabase db;

    public DataBaseManagerDatosUsuario(Context context) {
        DbHelperMiDiabetes helper = new DbHelperMiDiabetes(context);
        db = helper.getReadableDatabase();
    }

    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_NOMBRE + " text not null,"
            + CN_PESO + " text,"
            + CN_EDAD + " text,"
            + CN_DEBUT + " text,"
            + CN_MG_AYUNAS + " text,"
            + CN_MG_COMIDO + " text,"
            + CN_MG_NOCHE + " text,"
            + CN_mG_AYUNAS + " text,"
            + CN_mG_COMIDO + " text,"
            + CN_mG_NOCHE + " text,"
            + CN_mG_ESTANDAR + " text,"
            + CN_MG_ESTANDAR + " text,"
            + CN_HbA1c + " text,"
            + CN_IS + " text);";

    public Cursor cargarCursorDatosUsuario(){
        String[] columnas = new String[]{CN_ID,CN_NOMBRE,CN_PESO,CN_EDAD,CN_DEBUT,CN_MG_AYUNAS,CN_MG_COMIDO,CN_MG_NOCHE,CN_mG_AYUNAS,CN_mG_COMIDO,CN_mG_NOCHE, CN_mG_ESTANDAR, CN_MG_ESTANDAR,CN_HbA1c,CN_IS};
        return db.query(TABLE_NAME, columnas, null, null, null, null, null);
    }

    public void insertar (String nombre, String peso, String edad, String debut, String maxGlucosaAyunas, String maxGlucosaComido, String maxGlucosNoche,
                          String minGlucosaAyunas, String minglucosaComido, String minGlucosaNoche, String minGlucosaEstandar, String maxGlucosaEstandar, String hba1c, String indiceSensibilidad){
        db.insert(TABLE_NAME, null, generarContentValues(nombre, peso, edad, debut, maxGlucosaAyunas, maxGlucosaComido, maxGlucosNoche, minGlucosaAyunas, minglucosaComido,
                minGlucosaNoche, minGlucosaEstandar, maxGlucosaEstandar, hba1c, indiceSensibilidad));
    }

    public void modificar(String nombre, String peso, String edad, String debut, String maxGlucosaAyunas, String maxGlucosaComido, String maxGlucosNoche,
                          String minGlucosaAyunas, String minglucosaComido, String minGlucosaNoche, String minGlucosaEstandar, String maxGlucosaEstandar, String hba1c, String indiceSensibilidad) {
        db.update(TABLE_NAME, generarContentValues(nombre, peso, edad, debut, maxGlucosaAyunas, maxGlucosaComido, maxGlucosNoche, minGlucosaAyunas, minglucosaComido,
                minGlucosaNoche, minGlucosaEstandar, maxGlucosaEstandar, hba1c, indiceSensibilidad), CN_NOMBRE + "=?", new String[]{nombre});
    }

    public void eliminar(String id){
        db.delete(TABLE_NAME, CN_ID + "=?", new String[]{id});
    }

    private ContentValues generarContentValues(String nombre, String peso, String edad, String debut, String maxGlucosaAyunas, String maxGlucosaComido, String maxGlucosNoche,
                                               String minGlucosaAyunas, String minglucosaComido, String minGlucosaNoche, String minGlucosaEstandar, String maxGlucosaEstandar, String hba1c, String indiceSensibilidad) {
        ContentValues valores = new ContentValues();
        valores.put(CN_NOMBRE, nombre);
        valores.put(CN_PESO, peso);
        valores.put(CN_EDAD, edad);
        valores.put(CN_DEBUT, debut);
        valores.put(CN_MG_AYUNAS, maxGlucosaAyunas);
        valores.put(CN_MG_COMIDO, maxGlucosaComido);
        valores.put(CN_MG_NOCHE, maxGlucosNoche);
        valores.put(CN_mG_AYUNAS, minGlucosaAyunas);
        valores.put(CN_mG_COMIDO, minglucosaComido);
        valores.put(CN_mG_NOCHE, minGlucosaNoche);
        valores.put(CN_mG_ESTANDAR, minGlucosaEstandar);
        valores.put(CN_MG_ESTANDAR, maxGlucosaEstandar);
        valores.put(CN_HbA1c, hba1c);
        valores.put(CN_IS, indiceSensibilidad);

        return valores;
    }
}

