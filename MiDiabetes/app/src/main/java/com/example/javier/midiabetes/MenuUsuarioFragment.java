package com.example.javier.midiabetes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MenuUsuarioFragment extends Fragment{

    private TextView txtNombre, txtPeso, txtEdad, txtDebut, txtMaxAyunas, txtMaxComida, txtMaxNoche,txtMinAyunas,
            txtMinComida, txtMinNoche, txtMinEstandar, txtMaxEstandar, txtHbA1c, txtIndiceSensiblidad;
    private String identificador;
    private SharedPreferences prefs;
    private boolean reload = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_usuario, container, false);

        FloatingActionButton btnEditarPerfil = (FloatingActionButton) view.findViewById(R.id.btnEditarPerfil);
        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), EditarUsuarioActivity.class);
                i.putExtra("nombre", txtNombre.getText());
                i.putExtra("peso", txtPeso.getText());
                i.putExtra("edad", txtEdad.getText());
                i.putExtra("debut", txtDebut.getText());
                i.putExtra("glucosamaxayunas", txtMaxAyunas.getText());
                i.putExtra("glucosamaxcomido", txtMaxComida.getText());
                i.putExtra("glucosamaxnoche", txtMaxNoche.getText());
                i.putExtra("glucosaminayunas", txtMinAyunas.getText());
                i.putExtra("glucosamincomido", txtMinComida.getText());
                i.putExtra("glucosaminnoche", txtMinNoche.getText());
                i.putExtra("glucosaminestandar", txtMinEstandar.getText());
                i.putExtra("glucosamaxestandar", txtMaxEstandar.getText());
                i.putExtra("hba1c", txtHbA1c.getText());
                i.putExtra("indiceSensibilidad", txtIndiceSensiblidad.getText());
                i.putExtra("id", identificador);
                startActivity(i);
            }
        });
        prefs = getContext().getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        Button btnCerrarSesion = (Button) view.findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("sesionabierta", false);
                editor.remove("email");
                editor.remove("nombre");
                editor.apply();
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);

            }
        });

        txtNombre = (TextView) view.findViewById(R.id.txtNombre);
        txtNombre.setText(prefs.getString("nombre", ""));
        txtPeso = (TextView) view.findViewById(R.id.txtPeso);
        txtEdad = (TextView) view.findViewById(R.id.txtEdad);
        txtDebut = (TextView) view.findViewById(R.id.txtDebut);
        txtMaxAyunas = (TextView) view.findViewById(R.id.txtMaxGlucosaAyunas);
        txtMaxComida = (TextView) view.findViewById(R.id.txtMaxGlcuosaComido);
        txtMaxNoche = (TextView) view.findViewById(R.id.txtMaxGlucosaNoche);
        txtMinAyunas = (TextView) view.findViewById(R.id.txtMinGlucosaAyunas);
        txtMinComida = (TextView) view.findViewById(R.id.txtMinGlucosaComido);
        txtMinNoche = (TextView) view.findViewById(R.id.txtMinGlucosaNoche);
        txtMinEstandar = (TextView) view.findViewById(R.id.txtMinGlucosaEstandar);
        txtMaxEstandar = (TextView) view.findViewById(R.id.txtMaxGlucosaEstandar);
        txtHbA1c = (TextView) view.findViewById(R.id.txtHbA1c);
        txtIndiceSensiblidad = (TextView) view.findViewById(R.id.txtIndiceSensiblidad);

        DataBaseManagerDatosUsuario managerUsuario = new DataBaseManagerDatosUsuario(getContext());
        Cursor cursor = managerUsuario.cargarCursorDatosUsuario();

        if (cursor.moveToFirst()) {
            do {
                identificador = cursor.getString(0);
                txtNombre.setText(cursor.getString(1));
                txtPeso.setText(cursor.getString(2));
                txtEdad.setText(cursor.getString(3));
                txtDebut.setText(cursor.getString(4));
                txtMaxAyunas.setText(cursor.getString(5));
                txtMaxComida.setText(cursor.getString(6));
                txtMaxNoche.setText(cursor.getString(7));
                txtMinAyunas.setText(cursor.getString(8));
                txtMinComida.setText(cursor.getString(9));
                txtMinNoche.setText(cursor.getString(10));
                txtMinEstandar.setText(cursor.getString(11));
                txtMaxEstandar.setText(cursor.getString(12));
                txtIndiceSensiblidad.setText(cursor.getString(14));
            } while (cursor.moveToNext());
            cursor.close();
        }

        DataBaseManagerHemoglobina managerHemoglobinae = new DataBaseManagerHemoglobina(getContext());
        Cursor cursor2 = managerHemoglobinae.cargarCursorHemoglobina();

        if (cursor2.moveToLast()) {
                txtHbA1c.setText(cursor2.getString(1));
            cursor2.close();
        }
        return view;
    }

    public void onResume() {
        super.onResume();
        if(reload) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("return", 3);
            editor.apply();
            getActivity().finish();
            startActivity(getActivity().getIntent());
        }
    }

    public void onStop() {
        super.onStop();
        reload = true;
    }
}
