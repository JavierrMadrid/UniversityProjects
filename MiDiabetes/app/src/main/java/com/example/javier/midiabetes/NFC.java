package com.example.javier.midiabetes;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;

public class NFC extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            onNewIntent(getIntent());
        }
    }

    public void onResume() {
        super.onResume();
        if (getIntent() != null) {
            getDataNFC(getIntent());
        }
    }

    public void onPause() {
        super.onPause();
        finish();
    }

    public void getDataNFC(Intent intent) {
        NdefMessage[] msgs;

        String nfcTxtPayload = null;

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()) && tag != null) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];


                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];

                    NdefRecord nfcRecord = msgs[i].getRecords()[0];

                    nfcTxtPayload = new String(nfcRecord.getPayload());

                    nfcTxtPayload = nfcTxtPayload.substring(3);

                    Intent j = new Intent(this, InsertarComidaNFCActivity.class);
                    j.putExtra("textoNFC", nfcTxtPayload);
                    startActivity(j);
                    finish();
                }
            }
        }
    }
}