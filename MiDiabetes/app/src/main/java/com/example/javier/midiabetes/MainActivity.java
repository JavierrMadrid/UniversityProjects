package com.example.javier.midiabetes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button botonGlu = (Button) findViewById(R.id.btnGlucosa); //Definimos el boton
        botonGlu.setOnClickListener(this); //Le asignamos el evento onclick
        Button botonAli = (Button) findViewById(R.id.btnAlimentos); //Definimos el boton
        botonAli.setOnClickListener(this); //Le asignamos el evento onclick
        Button botonUsu = (Button) findViewById(R.id.btnUsuario); //Definimos el boton
        botonUsu.setOnClickListener(this); //Le asignamos el evento onclick
    }

    @Override
    public void onClick(View boton_pulsado) {
        if(boton_pulsado.getId() == R.id.btnGlucosa) {
            Intent i = new Intent(this, MenuGlucosa.class);
            startActivity(i);
        }else if(boton_pulsado.getId() == R.id.btnAlimentos) {
            Intent i = new Intent(this, MenuAlimentos.class);
            startActivity(i);
        }else if(boton_pulsado.getId() == R.id.btnUsuario) {
            Intent i = new Intent(this, MenuUsuario.class);
            startActivity(i);
        }
    }


}
