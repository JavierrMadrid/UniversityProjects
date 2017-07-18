package com.example.javier.midiabetes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ListaComidasFragment extends Fragment {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ListView listaComidas;
    SimpleCursorAdapter adapterCo, adapter;
    private SharedPreferences prefs;
    private String comida, peso, gramosxracion, raciones, ingredientes;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_lista_comidas,container,false);

        final DataBaseManagerComidas managerComidas = new DataBaseManagerComidas(getContext());
        listaComidas = (ListView) view.findViewById(R.id.listViewComidas);

        String[] from = new String[]{DataBaseManagerComidas.CN_COMIDA};
        int [] to = new int[]{android.R.id.text1};

        prefs = getContext().getSharedPreferences("Usuario", Context.MODE_PRIVATE);

        Cursor cursorCo = managerComidas.cargarCursorComidas();
        adapterCo = adapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_selectable_list_item, cursorCo, from, to, 0);

        AutoCompleteTextView editComida = (AutoCompleteTextView) view.findViewById(R.id.textBuscar);

            adapterCo.setFilterQueryProvider(new FilterQueryProvider() {

                @Override
                public Cursor runQuery(CharSequence constraint) {
                    Cursor cursor = managerComidas.cargarCursorComidas();
                    if (constraint == null || constraint.equals(""))
                    return cursor;

                    return managerComidas.filtrar(constraint.toString());
                }
            });
            adapterCo.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
                @Override
                public CharSequence convertToString(Cursor c) {
                    return c.getString(1);
                }
            });

        editComida.setThreshold(1);
        editComida.setAdapter(adapterCo);
        listaComidas.setAdapter(adapterCo);

        new Thread(new Runnable() {
            public void run() {
                try {
                    doInBackground();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        initActivity();

        return view;
    }

    private void initActivity(){
        final Intent activity = new Intent(getActivity(),DetallesComidaActivity.class);
        AdapterView.OnItemClickListener myclickListView = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) (listaComidas.getItemAtPosition(position));

                comida=cursor.getString(1);
                peso=cursor.getString(2);
                gramosxracion=cursor.getString(3);
                raciones=cursor.getString(4);
                ingredientes=cursor.getString(5);

                activity.putExtra("comida", comida);
                activity.putExtra("peso", peso);
                activity.putExtra("hidratos", gramosxracion);
                activity.putExtra("raciones", raciones);
                activity.putExtra("ingredientes", ingredientes);

                startActivity(activity);
            }
        };
        listaComidas.setOnItemClickListener(myclickListView);
    }

    public void doInBackground() throws JSONException {
        DataBaseManagerComidas managerComidas = new DataBaseManagerComidas(getContext());
        String jsonStr = downloadFileA();
        Log.e(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            JSONArray jsonComidas = new JSONArray(jsonStr);
            managerComidas.eliminar();
            try {
                for (int i = 0; i < jsonComidas.length(); i++) {
                    JSONObject c = jsonComidas.getJSONObject(i);

                    String nombre = c.getString("nombre");
                    String peso = c.getString("peso");
                    String hidratosxracion = c.getString("hidratosxracion");
                    String raciones = c.getString("raciones");
                    String ingredientes = c.getString("ingredientes");

                    managerComidas.insertar(nombre, peso, hidratosxracion, raciones, ingredientes);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String downloadFileA(){
        String email=prefs.getString("email", "");
        String response = null;
        String BBDDA_URL = "https://midiabetes.herokuapp.com/api/v1/comidas?email="+email;
        try {
            URL url = new URL(BBDDA_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}

