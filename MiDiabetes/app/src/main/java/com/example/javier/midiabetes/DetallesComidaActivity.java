package com.example.javier.midiabetes;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class DetallesComidaActivity extends AppCompatActivity{
    String comida, peso, gramosxracion, raciones, ingredientes, textoNFC;
    private NfcAdapter adapter;
    private PendingIntent pendingIntent;
    private IntentFilter writeTagFilters[];
    private Context context;
    private boolean modoescritura;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_comida);

        Bundle bundle = getIntent().getExtras();

        TextView textoComida= (TextView)findViewById(R.id.textComida);
        TextView textoPeso = (TextView)findViewById(R.id.textPeso);
        TextView textoGramosXracion = (TextView)findViewById(R.id.textGramosxRacion);
        TextView textoRaciones = (TextView)findViewById(R.id.textRaciones);
        TextView textoIngredientes = (TextView)findViewById(R.id.textIngredientes);

        textoComida.setText(bundle.getString("comida"));
        textoPeso.setText(bundle.getString("peso"));
        textoGramosXracion.setText(bundle.getString("hidratos"));
        textoRaciones.setText(bundle.getString("raciones"));
        textoIngredientes.setText(bundle.getString("ingredientes"));

        if(bundle.getBoolean("NFC")){
            textoNFC=bundle.getString("textoNFC");
            assert textoNFC != null;
            String[] separated = textoNFC.split(":");
            comida = separated[0];
            peso = separated[1];
            gramosxracion=separated[2];
            raciones=separated[3];
            ingredientes=separated[4];

            textoComida.setText(comida);
            textoPeso.setText(peso);
            textoGramosXracion.setText(gramosxracion);
            textoRaciones.setText(raciones);
            textoIngredientes.setText(ingredientes);
        }

        comida = textoComida.getText().toString();
        peso = textoPeso.getText().toString();
        gramosxracion = textoGramosXracion.getText().toString();
        raciones = textoRaciones.getText().toString();
        ingredientes = textoIngredientes.getText().toString();

        textoNFC=comida+":"+peso+":"+gramosxracion+":"+raciones+":"+ingredientes;

        adapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[] { tagDetected };
    }

    private void escribirNFC(String text, Tag tag) throws IOException, FormatException {

        NdefRecord[] records = { createRecord(text) };
        NdefMessage message = new NdefMessage(records);
        // Obtenemos una instancia de NDEF para la etiqueta.
        Ndef ndef = Ndef.get(tag);
        // Activamos la entrada/salida
        ndef.connect();
        // Escribimos los datos
        ndef.writeNdefMessage(message);
        // Cerramos la conexion
        ndef.close();

        finish();
    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang = "es";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;
        byte[] payLoad = new byte[1 + langLength + textLength];

        payLoad[0] = (byte) langLength;

        System.arraycopy(langBytes, 0, payLoad, 1, langLength);
        System.arraycopy(textBytes, 0, payLoad, 1 + langLength, textLength);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payLoad);
    }

    protected void onNewIntent(Intent intent){
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            Tag myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            try {
                if(myTag !=null){
                    escribirNFC(textoNFC, myTag);
                    Toast.makeText(getApplicationContext(), "Se ha escrito correctamente", Toast.LENGTH_LONG ).show();
                }
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error en la escritura", Toast.LENGTH_LONG ).show();
                e.printStackTrace();
            } catch (FormatException e) {
                Toast.makeText(getApplicationContext(), "Error en la escritura. Error de formato", Toast.LENGTH_LONG ).show();
                e.printStackTrace();
            }
        }
    }

    public void onPause(){
        super.onPause();
        WriteModeOff();
    }
    public void onResume(){
        super.onResume();
        WriteModeOn();
    }

    @SuppressLint("NewApi")
    private void WriteModeOn(){
        modoescritura = true;
        if (adapter!=null) {
            adapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
        }
    }

    @SuppressLint("NewApi")
    private void WriteModeOff(){
        modoescritura = false;
        if (adapter!=null) {
            adapter.disableForegroundDispatch(this);
        }
    }
}
