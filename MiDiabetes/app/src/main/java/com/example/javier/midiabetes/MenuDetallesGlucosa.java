package com.example.javier.midiabetes;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MenuDetallesGlucosa extends AppCompatActivity  implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private String antesodespues="", momento, id, seleccionSpinner;
    private EditText editFecha, editHora, editGlucosa, editComentario;
    private TextView txtFecha, txtHora, txtGlucosa, txtComentario, txtMomento;
    private Button btnEliminar, btnEditar, btnConfirmar;
    private String [] items;
    private RadioButton btnAntes, btnDespues;
    private Bundle bundle;
    private Spinner spinner;
    private int posItemSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_glucosa);

        bundle = getIntent().getExtras();

        txtFecha = (TextView) findViewById(R.id.txtViewFecha);
        txtHora = (TextView) findViewById(R.id.txtViewHora);
        txtGlucosa = (TextView) findViewById(R.id.txtViewGlucosa);
        txtComentario = (TextView) findViewById(R.id.txtViewComentarios);
        txtMomento = (TextView) findViewById(R.id.txtViewMomento);

        editFecha = (EditText) findViewById(R.id.editTextFecha);
        editHora = (EditText) findViewById(R.id.editTextHora);
        editGlucosa = (EditText) findViewById(R.id.editTextGlucosa);
        editComentario = (EditText) findViewById(R.id.editTextComentario);

        btnEliminar = (Button) findViewById(R.id.btnEliminarToma);
        btnEliminar.setOnClickListener(this);
        btnEditar = (Button) findViewById(R.id.btnEditarToma);
        btnEditar.setOnClickListener(this);
        btnConfirmar = (Button) findViewById(R.id.btnConfirmar);
        btnConfirmar.setOnClickListener(this);

        btnAntes = (RadioButton) findViewById(R.id.radioButton);
        btnDespues = (RadioButton) findViewById(R.id.radioButton2);

        items=getResources().getStringArray(R.array.momentoDelDia);

        spinner = (Spinner) findViewById(R.id.spinnerEditar);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        txtFecha.setText(bundle.getString("fecha"));
        txtHora.setText(bundle.getString("hora"));
        txtGlucosa.setText(bundle.getString("glucosa"));
        txtComentario.setText(bundle.getString("comentario"));
        txtMomento.setText(bundle.getString("momento"));
        antesodespues=bundle.getString("antesodespues");
        if(antesodespues.equalsIgnoreCase("antes")){
            btnAntes.setChecked(true);
        }else if (antesodespues.equalsIgnoreCase("despues")){
            btnDespues.setChecked(true);
        }
        id=bundle.getString("id");
        seleccionSpinner=txtMomento.getText().toString();
        posItemSpinner=comprobarPosition(seleccionSpinner);
    }

    private int comprobarPosition(String seleccionSpinner) {
        int pos=0;
           if (seleccionSpinner.equalsIgnoreCase("Desayuno"))
                pos=0;
        if (seleccionSpinner.equalsIgnoreCase("Almuerzo"))
                pos=1;
        if (seleccionSpinner.equalsIgnoreCase("Comida"))
                pos=2;
        if (seleccionSpinner.equalsIgnoreCase("Merienda"))
                pos=3;
        if (seleccionSpinner.equalsIgnoreCase("Cena"))
                pos=4;
        if (seleccionSpinner.equalsIgnoreCase("Ejercicio"))
                pos=5;
        if (seleccionSpinner.equalsIgnoreCase("Control nocturno"))
                pos=6;
        return pos;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnEliminarToma) {
            DataBaseManagerGlucosa managerGlucosa = new DataBaseManagerGlucosa(this);

            managerGlucosa.eliminar(String.valueOf(id));

            Toast toast1 = Toast.makeText(getApplicationContext(), "Muestra eliminada", Toast.LENGTH_SHORT);
            toast1.show();

            Intent i = new Intent(this, MenuGlucosa.class);
            startActivity(i);
            finish();

        }else if (v.getId() == R.id.btnEditarToma) {
            txtFecha.setVisibility(View.INVISIBLE);
            editFecha.setVisibility(View.VISIBLE);
            editFecha.setText(txtFecha.getText().toString());
            txtHora.setVisibility(View.INVISIBLE);
            editHora.setVisibility(View.VISIBLE);
            editHora.setText(txtHora.getText().toString());
            txtGlucosa.setVisibility(View.INVISIBLE);
            editGlucosa.setVisibility(View.VISIBLE);
            editGlucosa.setText(txtGlucosa.getText().toString());
            txtComentario.setVisibility(View.INVISIBLE);
            editComentario.setVisibility(View.VISIBLE);
            editComentario.setText(txtComentario.getText().toString());
            spinner.setVisibility(View.VISIBLE);
            txtMomento.setVisibility(View.INVISIBLE);
            btnEliminar.setVisibility(View.INVISIBLE);
            btnEditar.setVisibility(View.INVISIBLE);
            btnConfirmar.setVisibility(View.VISIBLE);
            btnAntes.setClickable(true);
            btnDespues.setClickable(true);
            spinner.setSelection(posItemSpinner);



        }else if (v.getId() == R.id.btnConfirmar) {
            DataBaseManagerGlucosa managerGlucosa = new DataBaseManagerGlucosa(this);
            if(btnAntes.isChecked()){
                antesodespues="antes";
            }else if (btnDespues.isChecked()){
                antesodespues="despues";
            }
            managerGlucosa.modificar(id, editFecha.getText().toString(), editHora.getText().toString(), antesodespues, momento, Integer.valueOf(editGlucosa.getText().toString()), editComentario.getText().toString());
            txtFecha.setVisibility(View.VISIBLE);
            editFecha.setVisibility(View.INVISIBLE);
            txtHora.setVisibility(View.VISIBLE);
            editHora.setVisibility(View.INVISIBLE);
            txtGlucosa.setVisibility(View.VISIBLE);
            editGlucosa.setVisibility(View.INVISIBLE);
            txtComentario.setVisibility(View.VISIBLE);
            editComentario.setVisibility(View.INVISIBLE);
            spinner.setVisibility(View.INVISIBLE);
            txtMomento.setVisibility(View.VISIBLE);
            btnEliminar.setVisibility(View.VISIBLE);
            btnEditar.setVisibility(View.VISIBLE);
            btnConfirmar.setVisibility(View.INVISIBLE);
            txtFecha.setText(editFecha.getText().toString());
            txtHora.setText(editHora.getText().toString());
            txtGlucosa.setText(editGlucosa.getText().toString());
            txtComentario.setText(editComentario.getText().toString());
            txtMomento.setText(momento);
            btnAntes.setClickable(false);
            btnDespues.setClickable(false);
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent i = new Intent(this, MenuGlucosa.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        momento=items[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
