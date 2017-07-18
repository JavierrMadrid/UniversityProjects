package com.example.javier.midiabetes;

import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class EditarUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editNombre, editPeso, editEdad, editDebut, editMaxAyunas, editMaxComida, editMaxNoche, editMinAyunas,
            editMinComido, editMinNoche, editMinEstandar, editMaxEstandar, editHbA1c, editIndiceSensibilidad;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        prefs = getSharedPreferences("Usuario", MODE_PRIVATE);
        Bundle bundle = getIntent().getExtras();

        FloatingActionButton btnGuardarPerfil = (FloatingActionButton) findViewById(R.id.btnGuardarDatos);
        btnGuardarPerfil.setOnClickListener(this);

        editNombre = (EditText)findViewById(R.id.editNombre);
        editPeso = (EditText)findViewById(R.id.editPeso);
        editEdad = (EditText)findViewById(R.id.editEdad);
        editDebut = (EditText) findViewById(R.id.editDebut);
        editMaxAyunas = (EditText)findViewById(R.id.editMaxAyunas);
        editMaxComida = (EditText)findViewById(R.id.editMaxComida);
        editMaxNoche = (EditText)findViewById(R.id.editMaxNoche);
        editMinAyunas = (EditText)findViewById(R.id.editMinAyunas);
        editMinComido = (EditText)findViewById(R.id.editMinComida);
        editMinNoche = (EditText)findViewById(R.id.editMinNoche);
        editMinEstandar = (EditText)findViewById(R.id.editMinEstandar);
        editMaxEstandar = (EditText)findViewById(R.id.editMaxEstandar);
        editHbA1c = (EditText)findViewById(R.id.editHbA1c);
        editIndiceSensibilidad = (EditText) findViewById(R.id.editIndiceSensibilidad);

        editNombre.setText(bundle.getString("nombre"));

        editPeso.setText(bundle.getString("peso"));
        editEdad.setText(bundle.getString("edad"));
        editDebut.setText(bundle.getString("debut"));
        editMaxAyunas.setText(bundle.getString("glucosamaxayunas"));
        editMaxComida.setText(bundle.getString("glucosamaxcomido"));
        editMaxNoche.setText(bundle.getString("glucosamaxnoche"));
        editMinAyunas.setText(bundle.getString("glucosaminayunas"));
        editMinComido.setText(bundle.getString("glucosamincomido"));
        editMinNoche.setText(bundle.getString("glucosaminnoche"));
        editMinEstandar.setText(bundle.getString("glucosaminestandar"));
        editMaxEstandar.setText(bundle.getString("glucosamaxestandar"));
        editHbA1c.setText(bundle.getString("hba1c"));
        editIndiceSensibilidad.setText(bundle.getString("indiceSensibilidad"));
        editNombre.setFocusable(false);

    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnGuardarDatos) {
            DataBaseManagerDatosUsuario managerUsuario = new DataBaseManagerDatosUsuario(this);

            String nombre = editNombre.getText().toString();
            String peso = editPeso.getText().toString();
            String edad = editEdad.getText().toString();
            String debut = editDebut.getText().toString();
            String maxGlucosaAyunas = editMaxAyunas.getText().toString();
            String maxGlucosaComido = editMaxComida.getText().toString();
            String maxGlucosaNoche = editMaxNoche.getText().toString();
            String minGlucosaAyunas = editMinAyunas.getText().toString();
            String minGlucosaComido = editMinComido.getText().toString();
            String minGlucosaNoche = editMinNoche.getText().toString();
            String minGluosaEstandar = editMinEstandar.getText().toString();
            String maxGlucosaEstandar = editMaxEstandar.getText().toString();
            String hba1c = editHbA1c.getText().toString();
            String indiceSensiblidad = editIndiceSensibilidad.getText().toString();

            if (!prefs.getBoolean("existeUsuario", false)) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "¡Información actualizada!", Toast.LENGTH_SHORT);
                toast1.show();
                managerUsuario.insertar(nombre, peso, edad, debut, maxGlucosaAyunas, maxGlucosaComido, maxGlucosaNoche, minGlucosaAyunas, minGlucosaComido, minGlucosaNoche,
                        minGluosaEstandar, maxGlucosaEstandar, hba1c, indiceSensiblidad);
                if (!editHbA1c.getText().toString().equals("")) {
                }
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("existeUsuario", true);
                editor.apply();
            } else {
                Toast toast1 = Toast.makeText(getApplicationContext(), "¡Información actualizada!", Toast.LENGTH_SHORT);
                toast1.show();
                managerUsuario.modificar(nombre, peso, edad, debut, maxGlucosaAyunas, maxGlucosaComido, maxGlucosaNoche, minGlucosaAyunas, minGlucosaComido, minGlucosaNoche,
                        minGluosaEstandar, maxGlucosaEstandar, hba1c, indiceSensiblidad);
                if (!editHbA1c.getText().toString().equals("")) {
                }
            }

            finish();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
