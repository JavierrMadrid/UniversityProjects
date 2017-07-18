package com.example.javier.midiabetes;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private Button signupButton;
    private EditText nameText, emailText, passwordText;
    private boolean validate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nameText = (EditText) findViewById(R.id.input_name);
        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);
        signupButton = (Button) findViewById(R.id.btn_signup);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        TextView signupLink = (TextView) findViewById(R.id.link_login);
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Registrarse");
        validate=validate();

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegistroActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creando Cuenta...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(validate){
                            onSignupSuccess();
                        }else {
                            onSignupFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 2000);
    }


    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        String nombre = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        DataBaseManagerUsuarios managerUsuario = new DataBaseManagerUsuarios(this);
        managerUsuario.insertar(nombre, email, password);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "No se ha podido registrar el usuario", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 2 || name.length() > 30) {
            nameText.setError("Nombre no válido. Debe contener al menos 2 caracteres y menos de 30");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Introduce un email valido");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            passwordText.setError("La contraseña debe contener entre 4 y 15 caracteres");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        DataBaseManagerUsuarios managerUsuarios = new DataBaseManagerUsuarios(this);
        Cursor cursor = managerUsuarios.cargarCursorUsuarios();
            if (cursor.moveToFirst()) {
                do {
                    String email2 = cursor.getString(2);
                    if (email.equalsIgnoreCase(email2)) {
                        emailText.setError("Ya hay una cuenta asociada a este email");
                        valid = false;
                    }
                } while (cursor.moveToNext());
                cursor.close();
        }

        return valid;
    }
}