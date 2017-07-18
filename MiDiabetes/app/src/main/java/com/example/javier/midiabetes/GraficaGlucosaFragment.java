package com.example.javier.midiabetes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import android.graphics.Color;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class GraficaGlucosaFragment extends Fragment {

    private DataBaseManagerGlucosa managerGlucosa;
    private DataBaseManagerDatosUsuario managerDatosUsuario;
    private boolean [] listaChecked = new boolean[8];
    private SharedPreferences prefs;
    private String [] items;
    private SimpleDateFormat sdf;
    private Calendar cal;
    private LineChart lineChart;
    private HashMap<Integer, String> labels;
    private Spinner spinner;
    private int maxGlucosaAyunas,maxGlucosaComido,maxGlucosaNoche,minGlucosaAyunas,minGlucosaComido,minGlucosaNoche, maxGlucosaEstandar, minGlucosaEstandar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grafica_glucosa, container, false);

        lineChart = (LineChart) view.findViewById(R.id.pieChart);

        cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        labels = new HashMap<>();
        labels.clear();

        managerGlucosa = new DataBaseManagerGlucosa(getContext());
        managerDatosUsuario = new DataBaseManagerDatosUsuario((getContext()));

        prefs = getContext().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        int itemSpinner=prefs.getInt("ItemSpinner", 0);

        listaChecked=RecuperarPreferencias(prefs);

        Button btnLimites = (Button) view.findViewById(R.id.btnLimites);
        btnLimites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Establecer límites:")
                        .setMultiChoiceItems(R.array.itemsLimites, listaChecked,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which,
                                                        boolean isChecked) {
                                        if (isChecked) {
                                            listaChecked[which]=true;
                                        } else if (listaChecked[which]) {
                                            listaChecked[which]=false;
                                        }
                                    }
                                })
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditarPreferencias(prefs);
                                mostrarLimites();
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

        items=getResources().getStringArray(R.array.NumeroDeDias);

        spinner = (Spinner) view.findViewById(R.id.spinnerGraficaGlucosa);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items);
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
            }
        });

        return view;
    }

    private void editarpreferencias2() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("ItemSpinner", spinner.getSelectedItemPosition());
        editor.apply();
    }

    private void mostrarPuntos(int tiempo, int diaOmes) throws ParseException {
        final ArrayList<Entry> entries = new ArrayList<>();
        String fecha, hora;
        Calendar cal2;
        String fechayhora;
        final Cursor cursor = managerGlucosa.cargarCursorGlucosa();
        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                fecha = cursor.getString(1);
                hora = cursor.getString(2);
                cal2 = Calendar.getInstance();
                fechayhora = fecha + " " + hora;

                String[] separated = fecha.split("/");
                String fechaLabel = separated[0]+"/"+separated[1];

                cal.setTime(sdf.parse(fechayhora));

                if (diaOmes == 0)
                    cal2.add(Calendar.DAY_OF_MONTH, tiempo);
                if (diaOmes == 1)
                    cal2.add(Calendar.MONTH, tiempo);

                if (cal2.before(cal) || (cal.get(cal.YEAR) == cal2.get(cal2.YEAR) && cal.get(cal.MONTH) == cal2.get(cal2.MONTH) && cal.get(cal.DAY_OF_MONTH) == cal2.get(cal2.DAY_OF_MONTH))) {
                    entries.add(new Entry(i, Float.valueOf(cursor.getString(5))));
                    labels.put(i,fechaLabel);
                    i++;
                }
            }while (cursor.moveToNext());
            cursor.close();
        }

        if(entries.size()>0) {

            LineDataSet dataset = new LineDataSet(entries, "Linea temporal de los niveles de glucosa");

            LineData data = new LineData(dataset);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setLabelCount(entries.size(), true);
            xAxis.setDrawGridLines(false);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                public String getFormattedValue(float value, AxisBase axis) {
                    if (entries.size() != 1) {
                        return labels.get((int) value);
                    } else return "";
                }
            });

            dataset.setDrawFilled(true);
            dataset.setFillColor(Color.YELLOW);
            dataset.setColors(Color.DKGRAY);
            dataset.setCircleColor(Color.BLACK);
            dataset.setCircleColorHole(Color.argb(255, 242, 165, 15));
            dataset.setCircleRadius(4);
            dataset.setValueTextSize(12);
            dataset.setValueTextColor(Color.BLACK);

            Description des = new Description();
            des.setText("Gráfica glucosa     ");

            lineChart.setDescription(des);
            lineChart.setData(data);
            lineChart.setTouchEnabled(true);
            lineChart.setPinchZoom(true);
            lineChart.setVisibleXRange(5f, 5f);
            lineChart.setDoubleTapToZoomEnabled(false);
            lineChart.setMaxHighlightDistance(15);
            lineChart.moveViewToX(entries.get(entries.size()-1).getX());
            lineChart.invalidate();
            lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    IMarker marker = new MyMarkerView(getActivity(), R.layout.markerview, e);
                    lineChart.setMarker(marker);
                }

                @Override
                public void onNothingSelected() {
                }
            });
            mostrarLimites();
        }
        refrescarLayout();
    }

    private void mostrarLimites() {
        Cursor cursor2 = managerDatosUsuario.cargarCursorDatosUsuario();

        if (cursor2.moveToFirst()) {
            do {
                if (!cursor2.getString(5).equals(""))
                    maxGlucosaAyunas = Integer.valueOf(cursor2.getString(5));
                if (!cursor2.getString(6).equals(""))
                    maxGlucosaComido = Integer.valueOf(cursor2.getString(6));
                if (!cursor2.getString(7).equals(""))
                    maxGlucosaNoche = Integer.valueOf(cursor2.getString(7));
                if (!cursor2.getString(8).equals(""))
                    minGlucosaAyunas = Integer.valueOf(cursor2.getString(8));
                if (!cursor2.getString(9).equals(""))
                    minGlucosaComido = Integer.valueOf(cursor2.getString(9));
                if (!cursor2.getString(10).equals(""))
                    minGlucosaNoche = Integer.valueOf(cursor2.getString(10));
                if (!cursor2.getString(11).equals(""))
                    minGlucosaEstandar = Integer.valueOf(cursor2.getString(11));
                if (!cursor2.getString(12).equals(""))
                    maxGlucosaEstandar = Integer.valueOf(cursor2.getString(12));
            } while (cursor2.moveToNext());
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

        LimitLine upper_limit3 = new LimitLine(maxGlucosaNoche, "Glucosa alta (noche)");
        upper_limit3.setLineWidth(4f);
        upper_limit3.enableDashedLine(10f, 10f, 0f);
        upper_limit3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        upper_limit3.setTextSize(10f);

        LimitLine upper_limit4 = new LimitLine(maxGlucosaEstandar, "Glucosa alta (Estándar)");
        upper_limit4.setLineWidth(4f);
        upper_limit4.enableDashedLine(10f, 10f, 0f);
        upper_limit4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        upper_limit4.setTextSize(10f);

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

        LimitLine lower_limit4 = new LimitLine(minGlucosaEstandar, "Glucosa baja (Estándar)");
        lower_limit4.setLineWidth(4f);
        lower_limit4.enableDashedLine(10f, 10f, 0f);
        lower_limit4.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit4.setTextSize(10f);


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        if (listaChecked[0])
            leftAxis.addLimitLine(upper_limit);
        if (listaChecked[1])
            leftAxis.addLimitLine(upper_limit2);
        if (listaChecked[2])
            leftAxis.addLimitLine(upper_limit3);
        if (listaChecked[3])
            leftAxis.addLimitLine(lower_limit);
        if (listaChecked[4])
            leftAxis.addLimitLine(lower_limit2);
        if (listaChecked[5])
            leftAxis.addLimitLine(lower_limit3);
        if (listaChecked[6])
            leftAxis.addLimitLine(upper_limit4);
        if (listaChecked[7])
            leftAxis.addLimitLine(lower_limit4);

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
        editor.putBoolean("Item7", listaChecked[6]);
        editor.putBoolean("Item8", listaChecked[7]);
        editor.apply();
    }

    private boolean [] RecuperarPreferencias(SharedPreferences prefs) {
        listaChecked[0] = prefs.getBoolean("Item1", false);
        listaChecked[1] = prefs.getBoolean("Item2", false);
        listaChecked[2] = prefs.getBoolean("Item3", false);
        listaChecked[3] = prefs.getBoolean("Item4", false);
        listaChecked[4] = prefs.getBoolean("Item5", false);
        listaChecked[5] = prefs.getBoolean("Item6", false);
        listaChecked[6] = prefs.getBoolean("Item7", false);
        listaChecked[7] = prefs.getBoolean("Item8", false);
        return listaChecked;
    }

}