package com.example.javier.midiabetes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManagerGlucosa {
    public static final String TABLE_NAME = "tablaGlucosa";
    public static final String CN_ID = "_id";
    public static final String CN_FECHA = "fecha";
    public static final String CN_HORA= "hora";
    public static final String CN_ANTESODESPUES ="antesodespues";
    public static final String CN_MOMENTO ="momento";
    public static final String CN_GLUCOSA = "glucosa";
    public static final String CN_COMENTARIO = "comentario";

    private DbHelperMiDiabetes helper;
    private SQLiteDatabase db;

    public DataBaseManagerGlucosa(Context context) {
        helper = new DbHelperMiDiabetes(context);
        db = helper.getReadableDatabase();
    }

    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_FECHA + " text not null,"
            + CN_HORA + " text not null,"
            + CN_ANTESODESPUES + " text not null,"
            + CN_MOMENTO + " text not null,"
            + CN_GLUCOSA + " text not null,"
            + CN_COMENTARIO + " text);";

    public Cursor cargarCursorGlucosa(){
        String [] columnas = new String[]{CN_ID,CN_FECHA,CN_HORA,CN_ANTESODESPUES,CN_MOMENTO,CN_GLUCOSA,CN_COMENTARIO};
        return db.query(TABLE_NAME, columnas, null,null,null,null,null);
    }

    public void insertar (String fecha, String hora, String antesodespues, String momento, int glucosa, String comentario){
        db.insert(TABLE_NAME, null, generarContentValues(fecha, hora, antesodespues, momento, glucosa, comentario));
    }

    public void eliminar(String id){
        db.delete(TABLE_NAME, CN_ID + "=?", new String[]{id});
    }

    public void modificar(String id, String fecha, String hora, String antesodespues, String momento, int glucosa, String comentario) {
        db.update(TABLE_NAME, generarContentValues(fecha, hora, antesodespues, momento, glucosa, comentario), CN_ID + "=?", new String[]{id});
    }

    private ContentValues generarContentValues(String fecha, String hora, String antesodespues, String momento, int glucosa, String comentario) {
        ContentValues valores = new ContentValues();
        valores.put(CN_FECHA, fecha);
        valores.put(CN_HORA, hora);
        valores.put(CN_ANTESODESPUES, antesodespues);
        valores.put(CN_MOMENTO, momento);
        valores.put(CN_GLUCOSA, glucosa);
        valores.put(CN_COMENTARIO, comentario);


        return valores;
    }
}
