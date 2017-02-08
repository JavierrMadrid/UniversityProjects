package com.example.javier.midiabetes;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.app.Activity;
import android.os.Parcelable;
import android.widget.TextView;

public class MenuNFC extends Activity {

    private TextView nfcMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Evaluamos si se viene de un Intent
        if (getIntent() != null) {
            //Llamamos al método processIntent para procesar el Intent
            onNewIntent(getIntent());
        }
    }

    public void onResume() {
        super.onResume();
        //Evaluamos si se viene de un Intent
        if (getIntent() != null) {
            //Llamamos la método processIntent para procesar el Intent
            onNewIntent(getIntent());
        }
    }

    public void onPause() {
        super.onPause();
        finish();
    }

    public void onNewIntent(Intent intent) {

        //Declaramos una variable de tipo Array de Mensajes NFC (NdefMessage)
        NdefMessage[] msgs;

        //Decalramos un variable para capturar el texto escrito en la etiqueta NFC
        //(llamado también Paypload data)
        String nfcTxtPayload = null;

        //Declaramos un objeto Tag para capturar el objeto Etiqueta NFC que viene
        //en Intent bajo la clave NfcAdapter.EXTRA_TAG
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        //Validamos que tenemos la acción del Intent sea de tipo NfcAdapter.ACTION_NDEF_DISCOVERED
        //ya que esta es la acctión definida en el nodo intent-filter que insertamos en el AndroidManifest.xml
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()) && tag != null) {
            //Capturamos el arreglo de mensajes que pueda contener la etiqeuta NFC
            //en un objeto array de tipo Parcelable que nos permita luego construir
            //un arreglo de mensajes NdefMessage
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (rawMsgs != null) {
                //Construimos el objeto array de mensajes NdefMessage
                msgs = new NdefMessage[rawMsgs.length];

                //Iteramos sobre todos los mensajes del arreglo de menasjes que puedan
                //venir en la Etiqueta NFC
                for (int i = 0; i < rawMsgs.length; i++) {
                    //Obtenemos el mensaje según el indice de la iteración
                    msgs[i] = (NdefMessage) rawMsgs[i];

                    //Obtenemos el primber registro NdefRecord del Mensaje en curso NdefMessage
                    NdefRecord nfcRecord = msgs[i].getRecords()[0];

                    //Constuimos un objeto String con el mensaje embebido en la Etiqueta en forma de Paypload data
                    nfcTxtPayload = new String(nfcRecord.getPayload());

                    //Omitimos los 2 primeros caracteres por que en ellos nos viene código ISO
                    //del lenguaje en el que viene el mensaje
                    nfcTxtPayload = nfcTxtPayload.substring(3);

                    //Actualizamos el TextView de la pantalla con el texto embebido en la
                    //Etiqueta en forma de Paypload data

                    Intent j = new Intent(this, MenuInsertarComida2.class);
                    j.putExtra("textoNFC", nfcTxtPayload);
                    startActivity(j);
                    finish();
                }
            }
        }
    }
}