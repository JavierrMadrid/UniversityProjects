package com.example.javier.midiabetes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class InicioFragment extends Fragment{
    private String fecha,hora,glucosa,antesodespues,momento,comentario, identificador;
    private String comida,peso,raciones,hidratos,ingredientes,fecha2;
    private DataBaseManagerGlucosa managerGlucosa;
    private DataBaseManagerNutricion managerNutricion;
    private ArrayList<ListData> datosGlucosa;
    private SimpleDateFormat sdf;
    private Calendar cal, cal2, cal3;
    private SharedPreferences prefs;
    private boolean reload = false;
    private RadioButton btnTodo, btnAlimentos, btnGlucosa;
    private ListView listview;
    private int eleccion=-1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        managerGlucosa = new DataBaseManagerGlucosa(getContext());
        managerNutricion = new DataBaseManagerNutricion(getContext());

        cal = cal3 = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        prefs = getContext().getSharedPreferences("Usuario", Context.MODE_PRIVATE);

        listview = (ListView) view.findViewById(R.id.listaInicio);
        RadioGroup radiogroup = (RadioGroup) view.findViewById(R.id.radioGroup2);
        btnTodo = (RadioButton) view.findViewById(R.id.radioButtonTodo);
        btnAlimentos = (RadioButton) view.findViewById(R.id.radioButtonComida);
        btnGlucosa = (RadioButton) view.findViewById(R.id.radioButtonGlucosa);

        final String[] opciones = new String [2];
        opciones[0]="Control de glucosa";
        opciones[1]="Control de ingesta de alimento";
        FloatingActionButton btnAñadir = (FloatingActionButton) view.findViewById(R.id.btnAgregarInicio);
        btnAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("¿Que quieres añadir?:")
                        .setSingleChoiceItems(opciones, -1, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        eleccion = which;
                                    }
                                })
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if(eleccion==0){
                                    Intent i = new Intent(getContext(), AgregarGlucosaActivity.class);
                                    startActivity(i);
                                    dialog.cancel();
                                }else if (eleccion==1){
                                    Intent i = new Intent(getContext(), InsertarComidaActivity.class);
                                    startActivity(i);
                                    dialog.cancel();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.create();
                builder.show();
            }
        });

        if(btnTodo.isChecked()){
            try {
                datosGlucosa = obtenerItems(-7, 1);
                ordenarLista();
                ListDataAdapter adapter = new ListDataAdapter(getActivity(), datosGlucosa);
                listview.setAdapter(adapter);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                try {
                    if(btnTodo.isChecked()){
                        datosGlucosa = obtenerItems(-7, 1);
                    }else if (btnAlimentos.isChecked()){
                        datosGlucosa = obtenerItems(-7, 3);
                    }else if(btnGlucosa.isChecked()){
                        datosGlucosa = obtenerItems(-7, 2);
                    }
                    ordenarLista();
                    ListDataAdapter adapter = new ListDataAdapter(getActivity(), datosGlucosa);
                    listview.setAdapter(adapter);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        final Intent activity = new Intent(getActivity(),DetallesGlucosaActivity.class);
        final Intent activity2 = new Intent(getActivity(),DetallesComidaActivity.class);
        AdapterView.OnItemClickListener myclickListView = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(datosGlucosa.get(position).getTipo()==1) {
                    identificador = datosGlucosa.get(position).getId();
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
                    activity.putExtra("glucosa", glucosa);
                    activity.putExtra("comentario", comentario);
                    activity.putExtra("id", identificador);

                    startActivity(activity);
                }
                if(datosGlucosa.get(position).getTipo()==2) {
                    comida = datosGlucosa.get(position).getComida();
                    peso = datosGlucosa.get(position).getPeso();
                    hidratos = datosGlucosa.get(position).getHidratos();
                    raciones = datosGlucosa.get(position).getRaciones();
                    ingredientes = datosGlucosa.get(position).getIngredientes();
                    fecha2 = datosGlucosa.get(position).getFecha2();

                    activity2.putExtra("comida", comida);
                    activity2.putExtra("peso", peso);
                    activity2.putExtra("hidratos", hidratos);
                    activity2.putExtra("raciones", raciones);
                    activity2.putExtra("ingredientes", ingredientes);
                    activity2.putExtra("fecha2", fecha2);

                    startActivity(activity2);
                }
            }
        };
        listview.setOnItemClickListener(myclickListView);

        return  view;
    }

    private void ordenarLista() {
        Collections.sort(datosGlucosa, new Comparator<ListData>(){
            @Override
            public int compare(ListData o1, ListData o2) {
                String fechayhora = null;
                String fechayhora2 = null;

                cal2 = Calendar.getInstance();
                cal3 = Calendar.getInstance();
                if(o1.getTipo()==1) {
                    fechayhora = o1.getFecha() + " " + o1.getHora();
                }else{
                    fechayhora = o1.getFecha2();
                }
                if(o2.getTipo()==1) {
                    fechayhora2 = o2.getFecha() + " " + o2.getHora();
                }else{
                    fechayhora2 = o2.getFecha2();
                }

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


    private ArrayList<ListData> obtenerItems(int tiempo, int check) throws ParseException {
        ArrayList<ListData> items = new ArrayList<>();

        Cursor cursor = managerGlucosa.cargarCursorGlucosa();
        Calendar cal2;
        String fechayhora;

        if(check==1 || check==2) {
            if (cursor.moveToFirst()) {
                do {
                    identificador = cursor.getString(0);
                    fecha = cursor.getString(1);
                    hora = cursor.getString(2);
                    antesodespues = cursor.getString(3);
                    momento = cursor.getString(4);
                    glucosa = cursor.getString(5);
                    comentario = cursor.getString(6);
                    fechayhora = fecha + " " + hora;
                    cal.setTime(sdf.parse(fechayhora));

                    cal2 = Calendar.getInstance();
                    cal2.add(Calendar.DAY_OF_MONTH, tiempo);

                    if (cal2.before(cal) || (cal.get(cal.YEAR) == cal2.get(cal2.YEAR) && cal.get(cal.MONTH) == cal2.get(cal2.MONTH) && cal.get(cal.DAY_OF_MONTH) == cal2.get(cal2.DAY_OF_MONTH))) {
                        items.add(0, new ListData(1, fecha, hora, antesodespues, momento, glucosa, comentario, identificador));
                    }

                } while (cursor.moveToNext());
                cursor.close();
            }
        }if (check==1 || check==3){
            Cursor cursor2 = managerNutricion.cargarCursorNutricion();

            if (cursor2.moveToFirst()) {
                do {
                    comida = cursor2.getString(1);
                    peso= cursor2.getString(2);
                    hidratos = cursor2.getString(3);
                    raciones = cursor2.getString(4);
                    ingredientes = cursor2.getString(5);
                    fecha2 = cursor2.getString(6);

                    cal3.setTime(sdf.parse(fecha2));

                    cal2 = Calendar.getInstance();
                    cal2.add(Calendar.DAY_OF_MONTH, tiempo);

                    if (cal2.before(cal3) || (cal3.get(cal.YEAR) == cal2.get(cal2.YEAR) && cal3.get(cal3.MONTH) == cal2.get(cal2.MONTH) && cal3.get(cal3.DAY_OF_MONTH) == cal2.get(cal2.DAY_OF_MONTH))) {
                        items.add(0, new ListData(2, fecha2, comida, peso, raciones, hidratos, ingredientes));
                    }

                } while (cursor2.moveToNext());
                cursor2.close();
            }
        }
        return items;
    }



    public void onResume() {
        super.onResume();
        if(reload) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("return", 0);
            editor.apply();
            getActivity().finish();
            startActivity(getActivity().getIntent());
        }
    }

    public void onStop() {
        super.onStop();
        reload = true;
    }
}
