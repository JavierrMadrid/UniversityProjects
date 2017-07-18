package com.example.javier.midiabetes;

import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ListData{
    private String fecha_publicacion, hora, antesodespues, momento, glucosa, comentario, id;
    private String fecha, comida, peso, raciones, hidratos, ingredientes;
    private int tipo;

    public ListData(int tipo, String fecha, String hora, String antesodespues, String momento, String glucosa, String comentario, String id) {
        this.tipo=tipo;
        this.fecha_publicacion = fecha;
        this.hora = hora;
        this.antesodespues= antesodespues;
        this.momento=momento;
        this.glucosa = glucosa;
        this.comentario=comentario;
        this.id=id;
    }

    public int getTipo() {
        return tipo;
    }

    public ListData(int tipo, String fecha, String comida, String peso, String raciones, String hidratos, String ingredientes){
        this.tipo=tipo;
        this.fecha=fecha;
        this.comida=comida;
        this.peso=peso;
        this.raciones=raciones;
        this.hidratos=hidratos;
        this.ingredientes=ingredientes;
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

    public String getComida() {
        return comida;
    }

    public void setComida(String comida) {
        this.comida = comida;
    }

    public String getRaciones() {
        return raciones;
    }

    public void setRaciones(String raciones) {
        this.raciones = raciones;
    }

    public String getHidratos() {
        return hidratos;
    }

    public void setHidratos(String hidratos) {
        this.hidratos = hidratos;
    }

    public String getFecha2() {
        return fecha;
    }


    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }
}
