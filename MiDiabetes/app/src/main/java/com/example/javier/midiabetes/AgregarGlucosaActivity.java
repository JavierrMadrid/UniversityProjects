package com.example.javier.midiabetes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

public class AgregarGlucosaActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private TextView txtFecha;
    private TextView txtHora;
    private NumberPicker np1, np2, np3;
    private RadioButton btnAntes, btnDespues;
    private EditText edTxtCom;
    private String [] items;

    String fecha, hora, comentario, g1, g2, g3, antesodespues="", momento;
    int glucosa;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_glucosa);

        np1 = (NumberPicker) findViewById(R.id.numberPicker1);
        np2 = (NumberPicker) findViewById(R.id.numberPicker2);
        np3 = (NumberPicker) findViewById(R.id.numberPicker3);
        txtFecha = (TextView) findViewById(R.id.txtFecha);
        txtHora = (TextView) findViewById(R.id.txtHora);
        btnAntes = (RadioButton) findViewById(R.id.radioButtonAntes);
        btnDespues = (RadioButton) findViewById(R.id.radioButtonDespues);
        edTxtCom = (EditText) findViewById(R.id.edTxtComentarios);
        Button btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(this);

        items=getResources().getStringArray(R.array.momentoDelDia);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerAñadir);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        np1.setMinValue(0);
        np1.setMaxValue(9);
        np2.setMinValue(0);
        np2.setMaxValue(9);
        np3.setMinValue(0);
        np3.setMaxValue(9);

        Calendar cal = Calendar.getInstance();
        int año = cal.get(Calendar.YEAR);
        int mes = 1 + cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        txtFecha.setText(dia +"/"+ mes +"/"+ año);
        txtHora.setText(hour +":"+ min);

    }

    public void onClick(View control_pulsado) {
        if(control_pulsado.getId() == R.id.btnGuardar) {
            DataBaseManagerGlucosa managerGlucosa = new DataBaseManagerGlucosa(this);

            fecha = txtFecha.getText().toString();
            hora = txtHora.getText().toString();
            g1 = String.valueOf(np1.getValue());
            g2 = String.valueOf(np2.getValue());
            g3 = String.valueOf(np3.getValue());
            glucosa= Integer.valueOf(g1+g2+g3);
            comentario = edTxtCom.getText().toString();

            Toast toast1 = Toast.makeText(getApplicationContext(),"Añadido correctamente", Toast.LENGTH_SHORT);
            toast1.show();

            if(btnAntes.isChecked()){
                antesodespues="Antes";
            }else if (btnDespues.isChecked()){
                antesodespues="Despues";
            }

            managerGlucosa.insertar(fecha, hora, antesodespues, momento, glucosa, comentario);
        }
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        momento=items[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
