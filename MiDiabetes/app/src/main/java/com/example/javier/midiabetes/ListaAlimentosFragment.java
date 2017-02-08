package com.example.javier.midiabetes;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ListaAlimentosFragment extends Fragment {

    private DataBaseManagerAlimentos managerAlimentos;
    private Cursor cursorAl;
    private ListView listaAlimentos;
    SimpleCursorAdapter adapterAl;
    private TextView txtBuscar;
    private Button btBuscar;
    private FloatingActionButton btnDescargarAlimentos;
    private String alimento, gramosXracion, consumo, racionesXconsumo, ig;
    private AutoCompleteTextView editAli;
    private ArrayAdapter<String> adaptadorAlimentos;
    private String[] alimentos;

    private static String BBDDA_URL = "http://midiabetes.esy.es//TablaHCAlimentosSQLite.sqlite";
    private final String PATH = "/data/data/com.example.javier.midiabetes/databases/";
    int downloadedSize = 0;
    int totalSize = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_lista_alimentos,container,false);

        managerAlimentos = new DataBaseManagerAlimentos(getContext());
        listaAlimentos = (ListView) view.findViewById(R.id.listViewAlimentos);

        txtBuscar = (TextView) view.findViewById(R.id.textBuscar);

        String[] from = new String[]{managerAlimentos.CN_AlIMENTO};
        int [] to = new int[]{android.R.id.text1};

        cursorAl = managerAlimentos.cargarCursorAlimentos();
        adapterAl = new SimpleCursorAdapter(getContext(), android.R.layout.simple_selectable_list_item, cursorAl, from, to, 0);
        listaAlimentos.setAdapter(adapterAl);

        rellenaGermenes();
        editAli=(AutoCompleteTextView) view.findViewById(R.id.textBuscar);
        adaptadorAlimentos=new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, alimentos);
        editAli.setThreshold(3);
        editAli.setAdapter(adaptadorAlimentos);

        btBuscar = (Button) view.findViewById(R.id.btnBuscar);
        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnBuscar) {
                    if (txtBuscar.getText().toString().isEmpty()) {
                        //adapterAl.changeCursor(cursorAl);
                    } else {
                        Cursor c = managerAlimentos.buscarAlimentos(txtBuscar.getText().toString());
                        adapterAl.changeCursor(c);
                    }
                }
            }
            });

        btnDescargarAlimentos = (FloatingActionButton) view.findViewById(R.id.btnDescargarListaA);
        btnDescargarAlimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                if(v.getId() == R.id.btnDescargarListaA) {
                    new Thread(new Runnable() {
                        public void run() {
                            downloadFileA();
                        }
                    }).start();
                    Toast.makeText(getContext(), "Se ha descargado la lista de alimentos correctamente", Toast.LENGTH_LONG).show();
                }
            }
            });

        initActivity();

        return view;
    }

    private void initActivity(){
        final Intent activity = new Intent(getActivity(),MenuDetallesAlimentos.class);
        AdapterView.OnItemClickListener myclickListView = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) (listaAlimentos.getItemAtPosition(position));

                alimento=cursor.getString(1);
                gramosXracion=cursor.getString(2);
                consumo=cursor.getString(3);
                racionesXconsumo=cursor.getString(4);
                ig=cursor.getString(5);

                activity.putExtra("alimento", alimento);
                activity.putExtra("gramosXracion", gramosXracion);
                activity.putExtra("consumo", consumo);
                activity.putExtra("racionesXconsumo", racionesXconsumo);
                activity.putExtra("ig", ig);
                startActivity(activity);
            }
        };
        listaAlimentos.setOnItemClickListener(myclickListView);
    }

    public void rellenaGermenes() {
        int nAlimentos=cursorAl.getCount();
        alimentos=new String[nAlimentos];

        int x=0;
        if (cursorAl.moveToFirst()) {
            do {
                alimentos[x]=cursorAl.getString(1);
                x++;
            } while (cursorAl.moveToNext());
        }
    }

    public void downloadFileA(){

        try {
            URL url = new URL(BBDDA_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            //connect
            urlConnection.connect();

            //create a new file, to save the downloaded file
            File file = new File(PATH+"TablaHCAlimentosSQLite.sqlite");

            FileOutputStream fileOutput = new FileOutputStream(file);

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
            }
            //close the output stream when complete //
            fileOutput.close();
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    // pb.dismiss(); // if you want close it..
                }
            });

        } catch (final MalformedURLException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}

