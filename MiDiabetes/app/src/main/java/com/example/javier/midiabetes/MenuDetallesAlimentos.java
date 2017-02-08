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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MenuDetallesAlimentos extends AppCompatActivity implements  View.OnClickListener{
    String alimentos, gramosXracion, consumo, raciones, ig, textoNFC;
    private NfcAdapter adapter;
    private PendingIntent pendingIntent;
    private IntentFilter writeTagFilters[];
    private Tag myTag;
    private Context context;
    private boolean modoescritura;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detalles_alimentos);

        Bundle bundle = getIntent().getExtras();

        TextView textoAlimento = (TextView)findViewById(R.id.textAlimento);
        TextView textoGramosXracion = (TextView)findViewById(R.id.textGrasmosXRacion);
        TextView textoConsumo = (TextView)findViewById(R.id.textConsumo);
        TextView textoRacionesXconsumo = (TextView)findViewById(R.id.textRacionesXHC);
        TextView textoIG = (TextView)findViewById(R.id.textIG);

        Button btnEscribir = (Button) findViewById(R.id.btnEscribir);
        btnEscribir.setOnClickListener(this);

        textoAlimento.setText(bundle.getString("alimento"));
        textoGramosXracion.setText(bundle.getString("gramosXracion"));
        textoConsumo.setText(bundle.getString("consumo"));
        textoRacionesXconsumo.setText(bundle.getString("racionesXconsumo"));
        textoIG.setText(bundle.getString("ig"));

        if(bundle.getBoolean("NFC")==true){
            textoNFC=bundle.getString("textoNFC");
            String[] separated = textoNFC.split(":");
            alimentos = separated[0];
            gramosXracion = separated[1];
            consumo=separated[2];
            raciones=separated[3];
            ig=separated[4];

            textoAlimento.setText(alimentos);
            textoGramosXracion.setText(gramosXracion);
            textoConsumo.setText(consumo);
            textoRacionesXconsumo.setText(raciones);
            textoIG.setText(ig);
        }

        alimentos = textoAlimento.getText().toString();
        gramosXracion = textoGramosXracion.getText().toString();
        consumo = textoConsumo.getText().toString();
        raciones = textoRacionesXconsumo.getText().toString();
        ig = textoIG.getText().toString();

        textoNFC=alimentos+":"+gramosXracion+":"+consumo+":"+raciones+":"+ig;

        adapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[] { tagDetected };
    }

    public void onClick(View control_pulsado) {
        if (control_pulsado.getId() == R.id.btnEscribir) {
            try {
                if(myTag==null){
                    Toast.makeText(getApplicationContext(), "Error. No se detecta la etiqueta NFC", Toast.LENGTH_LONG).show();
                }else{
                    escribir(textoNFC, myTag);
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

    private void escribir(String text, Tag tag) throws IOException, FormatException {

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

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payLoad);

        return recordNFC;
    }

    protected void onNewIntent(Intent intent){
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Toast.makeText(this, "Se ha detectado correctamente el tag NFC", Toast.LENGTH_LONG ).show();
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
