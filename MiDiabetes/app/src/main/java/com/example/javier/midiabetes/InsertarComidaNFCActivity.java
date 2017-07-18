package com.example.javier.midiabetes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class InsertarComidaNFCActivity extends AppCompatActivity implements View.OnClickListener{

    EditText edComida;
    EditText edPeso;
    EditText edHidratosxRacion;
    EditText edRaciones;
    EditText edIngredientes;

    String comida, peso, hidratosXracion, raciones, ingredientes, textoNFC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_comida);

        Bundle bundle = getIntent().getExtras();

        Button botonInsertar = (Button) findViewById(R.id.btnInsertar2);
        botonInsertar.setOnClickListener(this);

        edComida = (EditText)findViewById(R.id.txtAñadirComida2);
        edPeso = (EditText)findViewById(R.id.txtAñadirPeso2);
        edHidratosxRacion = (EditText)findViewById(R.id.txtAñadirHidratosxRacion2);
        edRaciones = (EditText)findViewById(R.id.txtAñadirRaciones2);
        edIngredientes = (EditText)findViewById(R.id.txtAñadirIngredientes2);

        if(bundle!=null){
            textoNFC=bundle.getString("textoNFC");
            assert textoNFC != null;
            String[] separated = textoNFC.split(":");
            comida = separated[0];
            peso = separated[1];
            hidratosXracion=separated[2];
            raciones=separated[3];
            ingredientes=separated[4];

            edComida.setText(comida);
            edPeso.setText(peso);
            edHidratosxRacion.setText(hidratosXracion);
            edRaciones.setText(raciones);
            edIngredientes.setText(ingredientes);
        }
    }

    public void onClick(View control_pulsado) {
        if(control_pulsado.getId() == R.id.btnInsertar2) {
            DataBaseManagerNutricion managerNutricion = new DataBaseManagerNutricion(this);

            comida = edComida.getText().toString();
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
            String fechayhora=dia+"/"+mes+"/"+año+" "+hour+":"+min;

            Toast toast1 = Toast.makeText(getApplicationContext(), comida+" añadido" , Toast.LENGTH_SHORT);
            toast1.show();

            managerNutricion.insertar(comida, peso, hidratosXracion, raciones, ingredientes, fechayhora);

            finish();
        }
    }
}
