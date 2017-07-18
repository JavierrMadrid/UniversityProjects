package com.example.javier.midiabetes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.LineChart;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.LinearLayout;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class GraficaHbA1cFragment extends Fragment {

    private DataBaseManagerHemoglobina managerHemoglobina;
    private LineChart lineChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grafica_hba1c, container, false);

        lineChart = (LineChart) view.findViewById(R.id.pieChart2);
        managerHemoglobina = new DataBaseManagerHemoglobina(getContext());

        try {
            mostrarPuntos();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        FloatingActionButton btnAñadir = (FloatingActionButton) view.findViewById(R.id.btnAñadirHemoglobina);
        btnAñadir.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Hba1c");
                builder.setMessage("Introduce el valor de la última toma de tu hemoglobina glicosilada");
                final EditText input = new EditText(getContext());
                input.setWidth(20);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                builder.setView(input);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Calendar cal = Calendar.getInstance();
                                int año = cal.get(Calendar.YEAR);
                                int mes = 1 + cal.get(Calendar.MONTH);
                                int dia = cal.get(Calendar.DAY_OF_MONTH);
                                String fecha = dia + "/" + mes + "/" + año;
                                String hba1c = input.getText().toString();
                                managerHemoglobina.insertar(hba1c, fecha);
                                try {
                                    mostrarPuntos();
                                } catch (ParseException e) {
                                    e.printStackTrace();
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

        return view;
    }

    private void mostrarPuntos() throws ParseException {
        final ArrayList<Entry> entries = new ArrayList<>();
        Cursor cursor = managerHemoglobina.cargarCursorHemoglobina();
        final HashMap<Integer, String> labels = new HashMap<>();
        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                entries.add(new Entry(i, Float.valueOf(cursor.getString(1))));
                labels.put(i, cursor.getString(2));
                i++;
            }  while (cursor.moveToNext());
            cursor.close();
        }

        if(entries.size()>0) {

            LineDataSet dataset = new LineDataSet(entries, "Linea temporal de los niveles de la hemoglobina glicosilada");

            LineData data = new LineData(dataset);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setLabelCount(entries.size(), true);
            xAxis.setDrawGridLines(false);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                public String getFormattedValue(float value, AxisBase axis) {
                        return labels.get((int) value);
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
            des.setText("Gráfica Hba1c");

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
                    IMarker marker = new MyMarkerView3(getActivity(), R.layout.markerview2, e);
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

}