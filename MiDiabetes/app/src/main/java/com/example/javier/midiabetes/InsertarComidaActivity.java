package com.example.javier.midiabetes;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class InsertarComidaActivity extends AppCompatActivity implements View.OnClickListener{

    Spinner comidas;
    EditText edPeso,edHidratosxRacion,edRaciones,edIngredientes;
    SimpleCursorAdapter adapterCo;

    String comida, peso, hidratosXracion, raciones, ingredientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_comida_nfc);

        Button botonInsertar = (Button) findViewById(R.id.btnAgregarConsumido);
        botonInsertar.setOnClickListener(this);

        String[] from = new String[]{DataBaseManagerComidas.CN_COMIDA};
        int [] to = new int[]{android.R.id.text1};

        final DataBaseManagerComidas managerComidas = new DataBaseManagerComidas(this);
        Cursor cursorCo = managerComidas.cargarCursorComidas();
        adapterCo = new SimpleCursorAdapter(this, android.R.layout.simple_selectable_list_item, cursorCo, from, to, 0);

        comidas = (Spinner) findViewById(R.id.spinnerInsertar);
        edPeso = (EditText)findViewById(R.id.txtAñadirPeso);
        edHidratosxRacion = (EditText)findViewById(R.id.txtAñadirHidratosxRacion);
        edRaciones = (EditText)findViewById(R.id.txtAñadirRaciones);
        edIngredientes = (EditText)findViewById(R.id.txtAñadirIngredientes);
        adapterCo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        comidas.setAdapter(adapterCo);
        comidas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) (comidas.getItemAtPosition(position));
                comida=cursor.getString(1);
                edPeso.setText(cursor.getString(2));
                edHidratosxRacion.setText(cursor.getString(3));
                edRaciones.setText(cursor.getString(4));
                edIngredientes.setText(cursor.getString(5));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void onClick(View control_pulsado) {
        if (control_pulsado.getId() == R.id.btnAgregarConsumido) {

            DataBaseManagerNutricion managerNutricion = new DataBaseManagerNutricion(this);

            peso = edPeso.getText().toString();
            hidratosXracion = edHidratosxRacion.getText().toString();
            raciones = edRaciones.getText().toString();
            ingredientes = edIngredientes.getText().toString();

            Calendar cal = Calendar.getInstance();
            int año = cal.get(Calendar.YEAR);
            int mes = 1 + cal.get(Calendar.MONTH);
            int dia = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);
            String fechayhora = dia + "/" + mes + "/" + año + " " + hour + ":" + min;

            Toast toast1 = Toast.makeText(getApplicationContext(), comida + " añadido", Toast.LENGTH_SHORT);
            toast1.show();

            managerNutricion.insertar(comida, peso, hidratosXracion, raciones, ingredientes, fechayhora);

            finish();
        }
    }
}
