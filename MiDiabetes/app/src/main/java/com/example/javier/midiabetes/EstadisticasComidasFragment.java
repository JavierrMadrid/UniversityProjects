package com.example.javier.midiabetes;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EstadisticasComidasFragment extends Fragment {
    private TextView text1, text2, text3, text4, text5, text6, text7, text8;
    private DataBaseManagerNutricion managerNutricion;
    private SimpleDateFormat sdf;
    private Calendar cal, cal3;
    private Spinner spinner;
    private ListView listview;
    private SharedPreferences prefs;
    private int diaomes, franjaTemporal;
    private double[] datos;
    private String[] items;
    private ArrayList<ListData> datosGlucosa;
    private double sumaRaciones = 0, sumaHidratos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estadisticas_comidas, container, false);
        cal = cal3 = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollview1);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        managerNutricion = new DataBaseManagerNutricion(getContext());

        prefs = getContext().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        int itemSpinner = prefs.getInt("ItemSpinner", 0);

        text1 = (TextView) view.findViewById(R.id.txtHidratos);
        text2 = (TextView) view.findViewById(R.id.txtRaciones);
        text3 = (TextView) view.findViewById(R.id.txtHidratosDia);
        text4 = (TextView) view.findViewById(R.id.txtRacionesDia);
        text5 = (TextView) view.findViewById(R.id.txtHidratosTotal);
        text6 = (TextView) view.findViewById(R.id.txtRacionesTotal);
        text7 = (TextView) view.findViewById(R.id.txtTomas);
        text8 = (TextView) view.findViewById(R.id.txtTomasDia);
        listview = (ListView) view.findViewById(R.id.listViewUltimasCom);

        items = getResources().getStringArray(R.array.NumeroDeDias);

        spinner = (Spinner) view.findViewById(R.id.spinnerEstadisticasGlucosa);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(itemSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (items[position].equalsIgnoreCase("Hoy")) {
                    franjaTemporal = 0;
                    diaomes = 0;
                    try {
                        datos = calcularEstadisticas(franjaTemporal, diaomes);
                        editarpreferencias();
                        datosGlucosa = obtenerItems(0);
                        ListDataAdapter adapter = new ListDataAdapter(getActivity(), datosGlucosa);
                        listview.setAdapter(adapter);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    text1.setText(String.valueOf(String.format("%.2f", datos[0])));
                    text2.setText(String.valueOf(String.format("%.2f", datos[1])));
                    text3.setText(String.valueOf(String.format("%.2f", datos[2])));
                    text4.setText(String.valueOf(String.format("%.2f", datos[3])));
                    text5.setText(String.valueOf(String.format("%.2f", datos[4])));
                    text6.setText(String.valueOf(String.format("%.2f", datos[5])));
                    text7.setText(String.valueOf((int) datos[6]));
                    text8.setText(String.valueOf((int) datos[7]));
                } else if (items[position].equalsIgnoreCase("Últimos 7 días")) {
                    franjaTemporal = -7;
                    diaomes = 0;
                    try {
                        datos = calcularEstadisticas(franjaTemporal, diaomes);
                        datosGlucosa = obtenerItems(0);
                        ListDataAdapter adapter = new ListDataAdapter(getActivity(), datosGlucosa);
                        listview.setAdapter(adapter);
                        editarpreferencias();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    text1.setText(String.valueOf(String.format("%.2f", datos[0])));
                    text2.setText(String.valueOf(String.format("%.2f", datos[1])));
                    text3.setText(String.valueOf(String.format("%.2f", datos[2])));
                    text4.setText(String.valueOf(String.format("%.2f", datos[3])));
                    text5.setText(String.valueOf(String.format("%.2f", datos[4])));
                    text6.setText(String.valueOf(String.format("%.2f", datos[5])));
                    text7.setText(String.valueOf((int) datos[6]));
                    text8.setText(String.valueOf((int) datos[7]));
                } else if (items[position].equalsIgnoreCase("Últimos 14 días")) {
                    franjaTemporal = -14;
                    diaomes = 0;
                    try {
                        datos = calcularEstadisticas(franjaTemporal, diaomes);
                        datosGlucosa = obtenerItems(0);
                        ListDataAdapter adapter = new ListDataAdapter(getActivity(), datosGlucosa);
                        listview.setAdapter(adapter);
                        editarpreferencias();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    text1.setText(String.valueOf(String.format("%.2f", datos[0])));
                    text2.setText(String.valueOf(String.format("%.2f", datos[1])));
                    text3.setText(String.valueOf(String.format("%.2f", datos[2])));
                    text4.setText(String.valueOf(String.format("%.2f", datos[3])));
                    text5.setText(String.valueOf(String.format("%.2f", datos[4])));
                    text6.setText(String.valueOf(String.format("%.2f", datos[5])));
                    text7.setText(String.valueOf((int) datos[6]));
                    text8.setText(String.valueOf((int) datos[7]));
                } else if (items[position].equalsIgnoreCase("Último mes")) {
                    franjaTemporal = -1;
                    diaomes = 1;
                    try {
                        datos = calcularEstadisticas(franjaTemporal, diaomes);
                        datosGlucosa = obtenerItems(0);
                        ListDataAdapter adapter = new ListDataAdapter(getActivity(), datosGlucosa);
                        listview.setAdapter(adapter);
                        editarpreferencias();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    text1.setText(String.valueOf(String.format("%.2f", datos[0])));
                    text2.setText(String.valueOf(String.format("%.2f", datos[1])));
                    text3.setText("-");
                    text4.setText("-");
                    text5.setText(String.valueOf(String.format("%.2f", datos[4])));
                    text6.setText(String.valueOf(String.format("%.2f", datos[5])));
                    text7.setText(String.valueOf((int) datos[6]));
                    text8.setText("-");
                } else if (items[position].equalsIgnoreCase("Últimos 3 meses")) {
                    franjaTemporal = -3;
                    diaomes = 1;
                    try {
                        datos = calcularEstadisticas(franjaTemporal, diaomes);
                        datosGlucosa = obtenerItems(0);
                        ListDataAdapter adapter = new ListDataAdapter(getActivity(), datosGlucosa);
                        listview.setAdapter(adapter);
                        editarpreferencias();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    text1.setText(String.valueOf(String.format("%.2f", datos[0])));
                    text2.setText(String.valueOf(String.format("%.2f", datos[1])));
                    text3.setText("-");
                    text4.setText("-");
                    text5.setText(String.valueOf(String.format("%.2f", datos[4])));
                    text6.setText(String.valueOf(String.format("%.2f", datos[5])));
                    text7.setText(String.valueOf((int) datos[6]));
                    text8.setText("-");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
    }

    private void editarpreferencias() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("ItemSpinner", spinner.getSelectedItemPosition());
        editor.apply();
    }

    private double[] calcularEstadisticas(int tiempo, int diaOmes) throws ParseException {
        int i = 0;
        double raciones, hidratos;
        Calendar cal2;
        String fecha;
        double[] datos = new double[8];
        Cursor cursor = managerNutricion.cargarCursorNutricion();
        if (cursor.moveToFirst()) {
            do {
                cal2 = Calendar.getInstance();
                fecha = cursor.getString(6);
                hidratos = Double.valueOf(cursor.getString(3));
                raciones = Double.valueOf(cursor.getString(4));

                cal.setTime(sdf.parse(fecha));

                if (diaOmes == 0)
                    cal2.add(Calendar.DAY_OF_MONTH, tiempo);
                if (diaOmes == 1)
                    cal2.add(Calendar.MONTH, tiempo);

                if (cal2.before(cal) || (cal.get(Calendar.YEAR) == cal2.get(cal2.YEAR) && cal.get(cal.MONTH) == cal2.get(cal2.MONTH) && cal.get(cal.DAY_OF_MONTH) == cal2.get(cal2.DAY_OF_MONTH))) {
                    sumaHidratos += hidratos;
                    sumaRaciones += raciones;
                    i++;
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (i > 0) {
            datos[0] = sumaHidratos / i;
            datos[1] = sumaRaciones / i;
            if(diaOmes==0 && franjaTemporal!=0){
                datos[2] = sumaHidratos / Math.abs(franjaTemporal);
                datos[3] = sumaRaciones / Math.abs(franjaTemporal);
            }else{
                datos[2] = sumaHidratos;
                datos[3] = sumaRaciones;
            }
            datos[4] = sumaHidratos;
            datos[5] = sumaRaciones;
            datos[6] = i;
            if(diaOmes==0 && franjaTemporal!=0){
                datos[7] = i/ Math.abs(franjaTemporal);
            }else{
                datos[7] = i;
            }
        }
        sumaHidratos = 0;
        sumaRaciones = 0;
        return datos;
    }

    private ArrayList<ListData> obtenerItems(int tiempo) throws ParseException {
        ArrayList<ListData> items = new ArrayList<>();
        Calendar cal2;
            Cursor cursor2 = managerNutricion.cargarCursorNutricion();

            if (cursor2.moveToFirst()) {
                do {
                    String comida = cursor2.getString(1);
                    String peso = cursor2.getString(2);
                    String hidratos = cursor2.getString(3);
                    String raciones = cursor2.getString(4);
                    String ingredientes = cursor2.getString(5);
                    String fecha2 = cursor2.getString(6);

                    cal3.setTime(sdf.parse(fecha2));

                    cal2 = Calendar.getInstance();
                    cal2.add(Calendar.DAY_OF_MONTH, tiempo);

                    if (cal2.before(cal3) || (cal3.get(cal.YEAR) == cal2.get(cal2.YEAR) && cal3.get(cal3.MONTH) == cal2.get(cal2.MONTH) && cal3.get(cal3.DAY_OF_MONTH) == cal2.get(cal2.DAY_OF_MONTH))) {
                        items.add(0, new ListData(3, fecha2, comida, peso, raciones, hidratos, ingredientes));
                    }

                } while (cursor2.moveToNext());
                cursor2.close();
            }
        return items;
    }
}
