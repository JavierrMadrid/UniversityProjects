package com.example.javier.midiabetes;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MenuUsuario extends AppCompatActivity implements View.OnClickListener{

    private TextView txtNombre, txtEdad, txtPeso, txtMaxAyunas, txtMaxComida, txtMaxNoche,txtMinAyunas,
            txtMinComida, txtMinNoche, txtHbA1c;
    private boolean existeTabla = false;
    private String identificador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);

        FloatingActionButton btnEditarPerfil = (FloatingActionButton) findViewById(R.id.btnEditarPerfil);
        btnEditarPerfil.setOnClickListener(this);

        txtNombre = (TextView) findViewById(R.id.txtNombre);
        txtPeso = (TextView) findViewById(R.id.txtPeso);
        txtEdad = (TextView) findViewById(R.id.txtEdad);
        txtMaxAyunas = (TextView) findViewById(R.id.txtMaxGlucosaAyunas);
        txtMaxComida = (TextView) findViewById(R.id.txtMaxGlcuosaComido);
        txtMaxNoche = (TextView) findViewById(R.id.txtMaxGlucosaNoche);
        txtMinAyunas = (TextView) findViewById(R.id.txtMinGlucosaAyunas);
        txtMinComida = (TextView) findViewById(R.id.txtMinGlucosaComido);
        txtMinNoche = (TextView) findViewById(R.id.txtMinGlucosaNoche);
        txtHbA1c = (TextView) findViewById(R.id.txtHbA1c);

        DataBaseManagerDatosUsuario managerUsuario = new DataBaseManagerDatosUsuario(this);
        Cursor cursor = managerUsuario.cargarCursorUsuario();

        if (cursor.moveToFirst()) {
            do {
                identificador=cursor.getString(0);
                txtNombre.setText(cursor.getString(1));
                txtPeso.setText(cursor.getString(2));
                txtEdad.setText(cursor.getString(3));
                txtMaxAyunas.setText(cursor.getString(4));
                txtMaxComida.setText(cursor.getString(5));
                txtMaxNoche.setText(cursor.getString(6));
                txtMinAyunas.setText(cursor.getString(7));
                txtMinComida.setText(cursor.getString(8));
                txtMinNoche.setText(cursor.getString(9));
                txtHbA1c.setText(cursor.getString(10));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (txtNombre.getText().length()!=0){
            existeTabla=true;
        }
    }

    public void onClick(View boton_pulsado) {
        if(boton_pulsado.getId() == R.id.btnEditarPerfil) {
            Intent i = new Intent(this, MenuEditarUsuario.class);
            i.putExtra("existeTabla", existeTabla);
            i.putExtra("nombre", txtNombre.getText());
            i.putExtra("peso", txtPeso.getText());
            i.putExtra("edad", txtEdad.getText());
            i.putExtra("glucosamaxayunas", txtMaxAyunas.getText());
            i.putExtra("glucosamaxcomido", txtMaxComida.getText());
            i.putExtra("glucosamaxnoche", txtMaxNoche.getText());
            i.putExtra("glucosaminayunas", txtMinAyunas.getText());
            i.putExtra("glucosamincomido", txtMinComida.getText());
            i.putExtra("glucosaminnoche", txtMinNoche.getText());
            i.putExtra("hba1c", txtHbA1c.getText());
            i.putExtra("id", identificador);
            finish();
            startActivity(i);
        }
    }
}
