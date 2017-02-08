package com.example.javier.midiabetes;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MenuEditarUsuario extends AppCompatActivity implements View.OnClickListener {

    private EditText editNombre, editEdad, editPeso, editHDesayuno, editHComida, editHCena, editMaxAyunas, editMaxComida, editMaxNoche, editMinAyunas,
            editMinComido, editMinNoche, editHbA1c;

    private String nombre, peso, edad, maxGlucosaAyunas, maxGlucosaComido, maxGlucosaNoche, minGlucosaAyunas, minGlucosaComido,
            minGlucosaNoche, hba1c, id;

    private boolean perfilCreado=false;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_editar_usuario);

        bundle = getIntent().getExtras();

        FloatingActionButton btnGuardarPerfil = (FloatingActionButton) findViewById(R.id.btnGuardarDatos);
        btnGuardarPerfil.setOnClickListener(this);

        Button btnEliminarUsuario = (Button) findViewById(R.id.btnEliminarUsuario);
        btnEliminarUsuario.setOnClickListener(this);

        editNombre = (EditText)findViewById(R.id.editNombre);
        editEdad = (EditText)findViewById(R.id.editEdad);
        editPeso = (EditText)findViewById(R.id.editPeso);
        editMaxAyunas = (EditText)findViewById(R.id.editMaxAyunas);
        editMaxComida = (EditText)findViewById(R.id.editMaxComida);
        editMaxNoche = (EditText)findViewById(R.id.editMaxNoche);
        editMinAyunas = (EditText)findViewById(R.id.editMinAyunas);
        editMinComido = (EditText)findViewById(R.id.editMinComida);
        editMinNoche = (EditText)findViewById(R.id.editMinNoche);
        editHbA1c = (EditText)findViewById(R.id.editHbA1c);

        if(bundle.getBoolean("existeTabla")==true) {
            editNombre.setText(bundle.getString("nombre"));
            editEdad.setText(bundle.getString("edad"));
            editPeso.setText(bundle.getString("peso"));
            editMaxAyunas.setText(bundle.getString("glucosamaxayunas"));
            editMaxComida.setText(bundle.getString("glucosamaxcomido"));
            editMaxNoche.setText(bundle.getString("glucosamaxnoche"));
            editMinAyunas.setText(bundle.getString("glucosaminayunas"));
            editMinComido.setText(bundle.getString("glucosamincomido"));
            editMinNoche.setText(bundle.getString("glucosaminnoche"));
            editHbA1c.setText(bundle.getString("hba1c"));
            id=bundle.getString("id");
            editNombre.setFocusable(false);
            btnEliminarUsuario.setVisibility(View.VISIBLE);
        }else{
            btnEliminarUsuario.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnGuardarDatos){
            DataBaseManagerDatosUsuario managerUsuario = new DataBaseManagerDatosUsuario(this);

            nombre = editNombre.getText().toString();
            peso = editPeso.getText().toString();
            edad = editEdad.getText().toString();
            maxGlucosaAyunas = editMaxAyunas.getText().toString();
            maxGlucosaComido = editMaxComida.getText().toString();
            maxGlucosaNoche = editMaxNoche.getText().toString();
            minGlucosaAyunas = editMinAyunas.getText().toString();
            minGlucosaComido = editMinComido.getText().toString();
            minGlucosaNoche = editMinNoche.getText().toString();
            hba1c = editHbA1c.getText().toString();

            if(bundle.getBoolean("existeTabla")==false) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Usuario "+nombre+" añadido" , Toast.LENGTH_SHORT);
                toast1.show();
                managerUsuario.insertar(nombre, peso, edad, maxGlucosaAyunas, maxGlucosaComido, maxGlucosaNoche, minGlucosaAyunas, minGlucosaComido, minGlucosaNoche, hba1c);
            } else {
                Toast toast1 = Toast.makeText(getApplicationContext(), "¡Información actualizada!" , Toast.LENGTH_SHORT);
                toast1.show();
                managerUsuario.modificar(nombre, peso, edad, maxGlucosaAyunas, maxGlucosaComido, maxGlucosaNoche, minGlucosaAyunas, minGlucosaComido, minGlucosaNoche, hba1c);
            }

            Intent i = new Intent(this, MenuUsuario.class);
            i.putExtra("perfilCreado", perfilCreado);
            finish();
            startActivity(i);

        }else if (v.getId()==R.id.btnEliminarUsuario) {
            DataBaseManagerDatosUsuario managerUsuario = new DataBaseManagerDatosUsuario(this);
            managerUsuario.eliminar(String.valueOf(id));

            Toast toast1 = Toast.makeText(getApplicationContext(), "Perfil eliminado", Toast.LENGTH_SHORT);
            toast1.show();

            Intent i = new Intent(this, MenuUsuario.class);
            startActivity(i);
            finish();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent i = new Intent(this, MenuUsuario.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
