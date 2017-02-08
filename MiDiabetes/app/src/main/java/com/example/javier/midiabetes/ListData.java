package com.example.javier.midiabetes;

import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Javier on 21/11/2016.
 */

public class ListData{
    private String fecha_publicacion;
    private String hora;
    private String antesodespues;
    private String momento;
    private String glucosa;
    private String id;
    private String comentario;

    public ListData(String fecha, String hora, String antesodespues, String momento, String glucosa, String comentario, String id) {
        this.fecha_publicacion = fecha;
        this.hora = hora;
        this.antesodespues= antesodespues;
        this.momento=momento;
        this.glucosa = glucosa;
        this.comentario=comentario;
        this.id=id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFecha() {
        return fecha_publicacion;
    }

    public void setFecha(String fecha) {
        this.fecha_publicacion = fecha;
    }

    public String getAntesodespues() {
        return antesodespues;
    }

    public void setAntesodespues(String antesodespues) {
        this.antesodespues = antesodespues;
    }

    public String getMomento() {
        return momento;
    }

    public void setMomento(String momento) {
        this.momento = momento;
    }

    public String getGlucosa() {
        return glucosa;
    }

    public void setGlucosa(String glucosa) {
        this.glucosa = glucosa;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getId() {
        return id;
    }

    public void setId(Cursor cursor) {
        this.id = id;
    }
}
