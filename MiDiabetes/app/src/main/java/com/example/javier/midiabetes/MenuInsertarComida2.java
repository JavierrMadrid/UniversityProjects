package com.example.javier.midiabetes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class MenuInsertarComida2 extends AppCompatActivity implements View.OnClickListener{

    EditText edComida;
    EditText edPeso;
    EditText edRaciones;
    EditText edCalorias;
    EditText edIngredientes;

    String alimento, gramosXracion, consumo, racionesXconsumo, ig, textoNFC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_insertar_comida2);

        Bundle bundle = getIntent().getExtras();

        Button botonInsertar = (Button) findViewById(R.id.btnInsertar2);
        botonInsertar.setOnClickListener(this);

        edComida = (EditText)findViewById(R.id.txtAñadirComida2);
        edPeso = (EditText)findViewById(R.id.txtAñadirRaciones2);
        edRaciones = (EditText)findViewById(R.id.txtAñadirPeso2);
        edCalorias = (EditText)findViewById(R.id.txtAñadirCalorias2);
        edIngredientes = (EditText)findViewById(R.id.txtAñadirIngredientes2);

        if(bundle.toString()!=null){
            textoNFC=bundle.getString("textoNFC");
            String[] separated = textoNFC.split(":");
            alimento = separated[0];
            gramosXracion = separated[1];
            consumo=separated[2];
            racionesXconsumo=separated[3];
            ig=separated[4];

            edComida.setText(alimento);
            edPeso.setText(gramosXracion);
            edRaciones.setText(consumo);
            edCalorias.setText(racionesXconsumo);
            edIngredientes.setText(ig);
        }
    }

    public void onPause(){
        super.onPause();
        finish();
    }

    public void onClick(View control_pulsado) {
        if(control_pulsado.getId() == R.id.btnInsertar2) {
            DataBaseManagerNutricion managerComidas = new DataBaseManagerNutricion(this);

            alimento = edComida.getText().toString();
            gramosXracion = edPeso.getText().toString();
            consumo = edRaciones.getText().toString();
            racionesXconsumo = edCalorias.getText().toString();
            ig = edIngredientes.getText().toString();

            Calendar cal = Calendar.getInstance();
            int año = cal.get(cal.YEAR);
            int mes = 1+cal.get(cal.MONTH);
            int dia = cal.get(cal.DAY_OF_MONTH);
            int hour = cal.get(cal.HOUR_OF_DAY);
            int min = cal.get(cal.MINUTE);
            String fechayhora=dia+"/"+mes+"/"+año+" "+hour+":"+min;

            Toast toast1 = Toast.makeText(getApplicationContext(), alimento+" añadido" , Toast.LENGTH_SHORT);
            toast1.show();

            managerComidas.insertar(alimento, gramosXracion, consumo, racionesXconsumo, ig, fechayhora);

            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
