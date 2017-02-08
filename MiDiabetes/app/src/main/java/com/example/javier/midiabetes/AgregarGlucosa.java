package com.example.javier.midiabetes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;

public class AgregarGlucosa extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private int año, mes, dia, hour, min;
    private TextView txtFecha;
    private TextView txtHora;
    private NumberPicker np1, np2, np3;
    private RadioButton btnAntes, btnDespues;
    private EditText edTxtCom;
    private Calendar cal;
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

        cal = Calendar.getInstance();
        año = cal.get(cal.YEAR);
        mes = 1+cal.get(cal.MONTH);
        dia = cal.get(cal.DAY_OF_MONTH);
        hour = cal.get(cal.HOUR_OF_DAY);
        min = cal.get(cal.MINUTE);
        txtFecha.setText(dia+"/"+mes+"/"+año);
        txtHora.setText(hour+":"+min);

    }

    public void onPause(){
        super.onPause();
        finish();
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
                antesodespues="antes";
            }else if (btnDespues.isChecked()){
                antesodespues="despues";
            }

            managerGlucosa.insertar(fecha, hora, antesodespues, momento, glucosa, comentario);

            Intent i = new Intent(this, MenuGlucosa.class);
            finish();
            startActivity(i);
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
