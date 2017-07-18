package com.example.javier.midiabetes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_SIGNUP = 0;
    private Button loginButton;
    private EditText emailText, passwordText;
    private String email2, pass, nombre;
    private boolean acceso=false;
    private boolean firstLogin=false;
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.btn_login);

        prefs = getSharedPreferences("Usuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if(prefs.getBoolean("sesionabierta", false)){
            editor.putInt("return", 0);
            editor.apply();
            onLoginSuccess();
        }
        loginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        TextView signupLink = (TextView) findViewById(R.id.link_signup);
        signupLink.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        boolean validate = validate();
        loginButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Verificando...");
        progressDialog.show();

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        DataBaseManagerUsuarios managerUsuarios = new DataBaseManagerUsuarios(this);
        Cursor cursor = managerUsuarios.cargarCursorUsuarios();
        if (validate) {
            if (cursor.moveToFirst()) {
                do {
                    nombre = cursor.getString(1);
                    email2 = cursor.getString(2);
                    pass = cursor.getString(3);
                    if (email.equals(email2) && password.equals(pass)) {
                        acceso = true;
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("nombre", nombre);
                        editor.putString("email", email2);
                        editor.apply();
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(acceso){
                            onLoginSuccess();
                        }else{
                            onLoginFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 2000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getBaseContext(), "¡Usuario registrado correctamente!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        Intent i = new Intent(this, MainActivity.class);
        finish();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("sesionabierta", true);
        editor.putInt("return", 0);
        editor.apply();
        startActivity(i);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "El email  o la contraseña introducidos son incorrectos", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
        passwordText.setText("");
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Introduce un email valido");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            passwordText.setError("La contraseña debe contener entre 4 y 10 caracteres");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}