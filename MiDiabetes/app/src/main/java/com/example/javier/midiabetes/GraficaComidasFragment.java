package com.example.javier.midiabetes;

import android.content.Context;
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
import android.widget.Spinner;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
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

public class GraficaComidasFragment extends Fragment {

    private DataBaseManagerNutricion managerNutricion;
    private boolean [] listaChecked = new boolean[7];
    private SharedPreferences prefs;
    private String [] items;
    private SimpleDateFormat sdf;
    private Calendar cal;
    private LineChart lineChart;
    private HashMap<Integer, String> labels;
    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grafica_comidas, container, false);

        lineChart = (LineChart) view.findViewById(R.id.pieChart3);

        cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        labels = new HashMap<>();
        labels.clear();

        managerNutricion = new DataBaseManagerNutricion(getContext());

        prefs = getContext().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        int itemSpinner=prefs.getInt("ItemSpinner", 0);

        listaChecked=RecuperarPreferencias(prefs);

        items=getResources().getStringArray(R.array.NumeroDeDias);

        spinner = (Spinner) view.findViewById(R.id.spinnerGraficaHidratos);
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
        String fecha;
        Calendar cal2;
        final Cursor cursor = managerNutricion.cargarCursorNutricion();
        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                cal2 = Calendar.getInstance();
                fecha=cursor.getString(6);
                String[] separated = fecha.split("/");
                String fechaLabel = separated[0]+"/"+separated[1];

                cal.setTime(sdf.parse(fecha));

                if (diaOmes == 0)
                    cal2.add(Calendar.DAY_OF_MONTH, tiempo);
                if (diaOmes == 1)
                    cal2.add(Calendar.MONTH, tiempo);

                if (cal2.before(cal) || (cal.get(cal.YEAR) == cal2.get(cal2.YEAR) && cal.get(cal.MONTH) == cal2.get(cal2.MONTH) && cal.get(cal.DAY_OF_MONTH) == cal2.get(cal2.DAY_OF_MONTH))) {
                    entries.add(new Entry(i, Float.valueOf(cursor.getString(3))));
                    labels.put(i,fechaLabel);
                    i++;
                }
            }while (cursor.moveToNext());
            cursor.close();
        }

        if(entries.size()>0) {

            LineDataSet dataset = new LineDataSet(entries, "Linea temporal de los hidratos consumidos");

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
            des.setText("Gráfica Hidratos     ");

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
                    IMarker marker = new MyMarkerView2(getActivity(), R.layout.markerview2, e);
                    lineChart.setMarker(marker);
                }

                @Override
                public void onNothingSelected() {
                }
            });
        }
        refrescarLayout();
    }

    private void refrescarLayout() {
        lineChart.setVisibility(View.GONE);
        lineChart.setVisibility(View.VISIBLE);
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