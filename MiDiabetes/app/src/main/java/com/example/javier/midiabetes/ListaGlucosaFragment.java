package com.example.javier.midiabetes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class ListaGlucosaFragment extends Fragment {
    private String fecha,hora,glucosa,antesodespues,momento,comentario, identificador;
    private ListView listview;
    private DataBaseManagerGlucosa managerGlucosa;
    private ArrayList<ListData> datosGlucosa;
    private ListDataAdapter adapter;
    private String [] items;
    private SimpleDateFormat sdf;
    private SharedPreferences prefs;
    private Calendar cal, cal2, cal3;
    private Spinner spinner;
    private View vi;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lista_glucosa, container, false);
        vi = inflater.inflate(R.layout.activity_elemento_lista, null);

        managerGlucosa = new DataBaseManagerGlucosa(getContext());

        cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        prefs = getContext().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        int itemSpinner=prefs.getInt("ItemSpinner", 0);

        listview = (ListView) view.findViewById(R.id.listaGlucosa);

        items=getResources().getStringArray(R.array.NumeroDeDias);

        final Intent activity = new Intent(getActivity(),DetallesGlucosaActivity.class);
        AdapterView.OnItemClickListener myclickListView = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                identificador=datosGlucosa.get(position).getId();
                fecha = datosGlucosa.get(position).getFecha();
                hora = datosGlucosa.get(position).getHora();
                antesodespues = datosGlucosa.get(position).getAntesodespues();
                momento = datosGlucosa.get(position).getMomento();
                glucosa = datosGlucosa.get(position).getGlucosa();
                comentario = datosGlucosa.get(position).getComentario();

                activity.putExtra("fecha", fecha);
                activity.putExtra("hora", hora);
                activity.putExtra("antesodespues", antesodespues);
                activity.putExtra("momento", momento);
                activity.putExtra("glucosa",glucosa);
                activity.putExtra("comentario", comentario);
                activity.putExtra("id", identificador);

                startActivity(activity);
            }
        };
        listview.setOnItemClickListener(myclickListView);

        spinner = (Spinner) view.findViewById(R.id.spinnerGlucosa);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner.setSelection(itemSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l){
                if (items[position].equalsIgnoreCase("Hoy")){
                    try {
                        datosGlucosa = obtenerItems(0,0);
                        ordenarLista();
                        adapter = new ListDataAdapter(getActivity(), datosGlucosa);
                        listview.setAdapter(adapter);
                        editarpreferencias();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (items[position].equalsIgnoreCase("Últimos 7 días")){
                    try {
                        datosGlucosa = obtenerItems(-7,0);
                        ordenarLista();
                        adapter = new ListDataAdapter(getActivity(), datosGlucosa);
                        listview.setAdapter(adapter);
                        editarpreferencias();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (items[position].equalsIgnoreCase("Últimos 14 días")){
                    try {
                        datosGlucosa = obtenerItems(-14,0);
                        ordenarLista();
                        adapter = new ListDataAdapter(getActivity(), datosGlucosa);
                        listview.setAdapter(adapter);
                        editarpreferencias();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (items[position].equalsIgnoreCase("Último mes")){
                    try {
                        datosGlucosa = obtenerItems(-1,1);
                        ordenarLista();
                        adapter = new ListDataAdapter(getActivity(), datosGlucosa);
                        listview.setAdapter(adapter);
                        editarpreferencias();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (items[position].equalsIgnoreCase("Últimos 3 meses")){
                    try {
                        datosGlucosa = obtenerItems(-3,1);
                        ordenarLista();
                        adapter = new ListDataAdapter(getActivity(), datosGlucosa);
                        listview.setAdapter(adapter);
                        editarpreferencias();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
    }

    private void ordenarLista() {
        Collections.sort(datosGlucosa, new Comparator<ListData>(){
            @Override
            public int compare(ListData o1, ListData o2) {
                String fechayhora;
                String fechayhora2;

                cal2 = Calendar.getInstance();
                cal3 = Calendar.getInstance();
                fechayhora = o1.getFecha() + " " + o1.getHora();
                fechayhora2 = o2.getFecha() + " " + o2.getHora();

                try {
                    cal2.setTime(sdf.parse(fechayhora));
                    cal3.setTime(sdf.parse(fechayhora2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return fechayhora2.compareTo(fechayhora);
            }
        });
    }

    private void editarpreferencias() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("ItemSpinner", spinner.getSelectedItemPosition());
        editor.apply();
    }


    private ArrayList<ListData> obtenerItems(int tiempo, int diaOmes) throws ParseException {
        ArrayList<ListData> items = new ArrayList<>();

        Cursor cursor = managerGlucosa.cargarCursorGlucosa();
        Calendar cal2;
        String fechayhora;

        if (cursor.moveToFirst()) {
            do {
                identificador= cursor.getString(0);
                fecha= cursor.getString(1);
                hora= cursor.getString(2);
                antesodespues= cursor.getString(3);
                momento= cursor.getString(4);
                glucosa= cursor.getString(5);
                comentario= cursor.getString(6);
                cal2 = Calendar.getInstance();
                fechayhora=fecha+" "+hora;

                cal.setTime(sdf.parse(fechayhora));

                if(diaOmes==0)
                    cal2.add(Calendar.DAY_OF_MONTH, tiempo);
                if(diaOmes==1)
                    cal2.add(Calendar.MONTH, tiempo);

                if(cal2.before(cal) || (cal.get(cal.YEAR) == cal2.get(cal2.YEAR) && cal.get(cal.MONTH) == cal2.get(cal2.MONTH) && cal.get(cal.DAY_OF_MONTH) == cal2.get(cal2.DAY_OF_MONTH))){
                    items.add(0,new ListData(1, fecha, hora, antesodespues, momento,  glucosa, comentario, identificador));
                }

            } while (cursor.moveToNext());
            cursor.close();
        }

        return items;
    }
}
