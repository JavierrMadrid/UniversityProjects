package com.example.javier.midiabetes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import android.graphics.Color;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class GraficasFragment extends Fragment {

    private Cursor cursor, cursor2;
    private DataBaseManagerGlucosa managerGlucosa;
    private DataBaseManagerDatosUsuario managerDatosUsuario;
    private Button btnLimites;
    private boolean [] listaChecked = new boolean[7];
    private SharedPreferences prefs;
    private String [] items;
    private SimpleDateFormat sdf;
    private Calendar cal;
    private LineChart lineChart;
    private LineData data;
    private  ArrayList<String> labels;
    private ArrayList<Entry> entries;
    private  LineDataSet dataset;
    private Spinner spinner;
    private int maxGlucosaAyunas,maxGlucosaComido,maxGlucosaNoche,minGlucosaAyunas,minGlucosaComido,minGlucosaNoche;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_graficas, container, false);

        lineChart = (LineChart) view.findViewById(R.id.pieChart);

        cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        managerGlucosa = new DataBaseManagerGlucosa(getContext());
        managerDatosUsuario = new DataBaseManagerDatosUsuario((getContext()));

        prefs = getContext().getSharedPreferences("MisPreferencias",getContext().MODE_PRIVATE);
        int itemSpinner=prefs.getInt("ItemSpinner", 0);

        listaChecked=RecuperarPreferencias(prefs);

        btnLimites = (Button) view.findViewById(R.id.btnLimites);
        btnLimites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Set the dialog title

                builder.setTitle("Establecer límites:")
                        // Specify the list array, the items to be selected by default (null for none),
                        // and the listener through which to receive callbacks when items are selected
                        .setMultiChoiceItems(R.array.itemsLimites, listaChecked,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which,
                                                        boolean isChecked) {
                                        if (isChecked) {
                                            // If the user checked the item, add it to the selected items
                                            listaChecked[which]=true;
                                        } else if (listaChecked[which]) {
                                            // Else, if the item is already in the array, remove it
                                            listaChecked[which]=false;
                                        }
                                    }
                                })
                        // Set the action buttons
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                              EditarPreferencias(prefs);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            }
                        });
                builder.create();
                builder.show();
            }
        });

        items=getResources().getStringArray(R.array.NumeroDeDias);

        spinner = (Spinner) view.findViewById(R.id.spinnerGraficaGlucosa);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner.setSelection(itemSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l){
                if (items[position].equalsIgnoreCase("Hoy")){
                    try {
                        mostrarPuntos(0,0);
                        editarpreferencias2();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (items[position].equalsIgnoreCase("Últimos 7 días")){
                    try {
                        mostrarPuntos(-7,0);
                        editarpreferencias2();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (items[position].equalsIgnoreCase("Últimos 14 días")){
                    try {
                        mostrarPuntos(-14,0);
                        editarpreferencias2();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (items[position].equalsIgnoreCase("Último mes")){
                    try {
                       mostrarPuntos(-1,1);
                        editarpreferencias2();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (items[position].equalsIgnoreCase("Últimos 3 meses")){
                    try {
                        mostrarPuntos(-3,1);
                        editarpreferencias2();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                try {
                    mostrarPuntos(-3,1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void editarpreferencias2() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("ItemSpinner", spinner.getSelectedItemPosition());
        editor.commit();
    }

    private void mostrarPuntos(int tiempo, int diaOmes) throws ParseException {
        entries = new ArrayList<>();
        String fecha, hora;
        Calendar cal2;
        String fechayhora="";
        cursor = managerGlucosa.cargarCursorGlucosa();
        labels = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                fecha = cursor.getString(1);
                hora = cursor.getString(2);
                cal2 = Calendar.getInstance();
                fechayhora = fecha + " " + hora;

                cal.setTime(sdf.parse(fechayhora));

                if (diaOmes == 0)
                    cal2.add(Calendar.DAY_OF_MONTH, tiempo);
                if (diaOmes == 1)
                    cal2.add(Calendar.MONTH, tiempo);

                if (cal2.before(cal) || (cal.get(cal.YEAR) == cal2.get(cal2.YEAR) && cal.get(cal.MONTH) == cal2.get(cal2.MONTH) && cal.get(cal.DAY_OF_MONTH) == cal2.get(cal2.DAY_OF_MONTH))) {
                    entries.add(new Entry(Float.valueOf(cursor.getString(5)), i));
                    labels.add(cursor.getString(1));
                    i++;
                }
            }  while (cursor.moveToNext());
            cursor.close();
        }

        dataset = new LineDataSet(entries, "Linea temporal de los niveles de glucosa");

        data = new LineData(labels, dataset);
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);
        dataset.setFillColor(Color.YELLOW);
        dataset.setColors(new int[]{Color.DKGRAY});
        dataset.setCircleColor(Color.BLACK);
        dataset.setCircleColorHole(Color.argb(255,242,165,15));
        dataset.setCircleRadius(4);
        dataset.setValueTextSize(12);
        dataset.setValueTextColor(Color.BLACK);

        lineChart.setData(data);
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setVisibleXRange(9f, 9f);

        cursor2 = managerDatosUsuario.cargarCursorUsuario();

        if (cursor2.moveToFirst()) {
            do {
                maxGlucosaAyunas = Integer.valueOf(cursor2.getString(4));
                maxGlucosaComido = Integer.valueOf(cursor2.getString(5));
                maxGlucosaNoche = Integer.valueOf(cursor2.getString(6));
                minGlucosaAyunas = Integer.valueOf(cursor2.getString(7));
                minGlucosaComido = Integer.valueOf(cursor2.getString(8));
                minGlucosaNoche = Integer.valueOf(cursor2.getString(9));
            }  while (cursor2.moveToNext());
            cursor2.close();
        }


        LimitLine upper_limit = new LimitLine(maxGlucosaAyunas, "Glucosa alta (en ayunas)");
        upper_limit.setLineWidth(4f);
        upper_limit.enableDashedLine(10f, 10f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(10f);

        LimitLine upper_limit2 = new LimitLine(maxGlucosaComido, "Glucosa alta (después de comer)");
        upper_limit2.setLineWidth(4f);
        upper_limit2.enableDashedLine(10f, 10f, 0f);
        upper_limit2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit2.setTextSize(10f);

        LimitLine  upper_limit3 = new LimitLine(maxGlucosaNoche, "Glucosa alta (noche)");
        upper_limit3.setLineWidth(4f);
        upper_limit3.enableDashedLine(10f, 10f, 0f);
        upper_limit3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        upper_limit3.setTextSize(10f);

        LimitLine lower_limit = new LimitLine(minGlucosaAyunas, "Glucosa baja (en ayunas)");
        lower_limit.setLineWidth(4f);
        lower_limit.enableDashedLine(10f, 10f, 0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(10f);

        LimitLine lower_limit2 = new LimitLine(minGlucosaComido, "Glucosa baja (después de comer)");
        lower_limit2.setLineWidth(4f);
        lower_limit2.enableDashedLine(10f, 10f, 0f);
        lower_limit2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit2.setTextSize(10f);

        LimitLine lower_limit3 = new LimitLine(minGlucosaNoche, "Glucosa baja (noche)");
        lower_limit3.setLineWidth(4f);
        lower_limit3.enableDashedLine(10f, 10f, 0f);
        lower_limit3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit3.setTextSize(10f);


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        if(listaChecked[0]==true)
            leftAxis.addLimitLine(upper_limit);
        if(listaChecked[1]==true)
            leftAxis.addLimitLine(upper_limit2);
        if(listaChecked[2]==true)
            leftAxis.addLimitLine(upper_limit3);
        if(listaChecked[3]==true)
            leftAxis.addLimitLine(lower_limit);
        if(listaChecked[4]==true)
            leftAxis.addLimitLine(lower_limit2);
        if(listaChecked[5]==true)
            leftAxis.addLimitLine(lower_limit3);

        leftAxis.setDrawLimitLinesBehindData(true);

        refrescarLayout();
    }

    private void refrescarLayout() {
        lineChart.setVisibility(View.GONE);
        lineChart.setVisibility(View.VISIBLE);
    }

    private void EditarPreferencias(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("Item1", listaChecked[0]);
        editor.putBoolean("Item2", listaChecked[1]);
        editor.putBoolean("Item3", listaChecked[2]);
        editor.putBoolean("Item4", listaChecked[3]);
        editor.putBoolean("Item5", listaChecked[4]);
        editor.putBoolean("Item6", listaChecked[5]);
        editor.commit();
    }

    private boolean [] RecuperarPreferencias(SharedPreferences prefs) {
        listaChecked[0] = prefs.getBoolean("Item1", false);
        listaChecked[1] = prefs.getBoolean("Item2", false);
        listaChecked[2] = prefs.getBoolean("Item3", false);
        listaChecked[3] = prefs.getBoolean("Item4", false);
        listaChecked[4] = prefs.getBoolean("Item5", false);
        listaChecked[5] = prefs.getBoolean("Item6", false);
        return listaChecked;
    }
}