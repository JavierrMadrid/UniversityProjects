package com.example.javier.midiabetes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EstadisticasFragment extends Fragment {
    private DataBaseManagerGlucosa managerGlucosa;
    private SimpleDateFormat sdf;
    private Calendar cal;
    private int sumaGlucosas=0, sumaGlucosas2=0, sumaGlucosas3=0;
    private TextView text1, text2, text3, text4, text5, text6, text7, text8, text9, text10;
    private String [] items, items2;
    private SharedPreferences prefs;
    private int [] datos;
    private double hemoglobina;
    private Spinner spinner, spinner2;
    private String momentodeldia;
    private int diaomes, franjaTemporal;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_estadisticas,container,false);

        cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        managerGlucosa = new DataBaseManagerGlucosa(getContext());

        prefs = getContext().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        int itemSpinner=prefs.getInt("ItemSpinner", 0);
        int itemSpinner2=prefs.getInt("ItemSpinner2", 0);

        momentodeldia=prefs.getString("Momento", "");

        text1 = (TextView) view.findViewById(R.id.txtMedia);
        text2 = (TextView) view.findViewById(R.id.txtMaxima);
        text3 = (TextView) view.findViewById(R.id.txtMinima);
        text4 = (TextView) view.findViewById(R.id.txtMediaDespues);
        text5 = (TextView) view.findViewById(R.id.txtMinimaDespues);
        text6 = (TextView) view.findViewById(R.id.txtMaximaDespues);
        text7 = (TextView) view.findViewById(R.id.txtHbA1cEstimada);
        text8 = (TextView) view.findViewById(R.id.txtMediaAntes);
        text9 = (TextView) view.findViewById(R.id.txtMinimaAntes);
        text10 = (TextView) view.findViewById(R.id.txtMaximaAntes);

        TextView btnDialogo = (TextView) view.findViewById(R.id.btnInformacion);
        btnDialogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Set the dialog title

                builder.setTitle("Establecer límites:").setMessage("Este cálculo es una estimación del valor HbA1c (a partir de las tomas de glucosa que ha ido realizando el usuario) el cual puede no ser exacto. " +
                        "\n\nSe ofrece como una ayuda, en ningún caso para sustituir o contrariar el criterio de un médico.")
                        .setTitle("Advertencia").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
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
        items2=getResources().getStringArray(R.array.momentoDelDia);

        spinner = (Spinner) view.findViewById(R.id.spinnerEstadisticasGlucosa);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(itemSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l){
                if (items[position].equalsIgnoreCase("Hoy")){
                    franjaTemporal=0;
                    diaomes=0;
                    try {
                        datos=franjaTemporal(franjaTemporal,diaomes);
                        editarpreferencias();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    text1.setText(String.valueOf(datos[0]));
                    text2.setText(String.valueOf(datos[1]));
                    text3.setText(String.valueOf(datos[2]));
                    text4.setText(String.valueOf(datos[3]));
                    text5.setText(String.valueOf(datos[4]));
                    text6.setText(String.valueOf(datos[5]));
                    text8.setText(String.valueOf(datos[6]));
                    text9.setText(String.valueOf(datos[7]));
                    text10.setText(String.valueOf(datos[8]));
                    text7.setText("-");
                } else if (items[position].equalsIgnoreCase("Últimos 7 días")){
                    franjaTemporal=-7;
                    diaomes=0;
                    try {
                        datos=franjaTemporal(franjaTemporal,diaomes);
                        editarpreferencias();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    text1.setText(String.valueOf(datos[0]));
                    text2.setText(String.valueOf(datos[1]));
                    text3.setText(String.valueOf(datos[2]));
                    text4.setText(String.valueOf(datos[3]));
                    text5.setText(String.valueOf(datos[4]));
                    text6.setText(String.valueOf(datos[5]));
                    text8.setText(String.valueOf(datos[6]));
                    text9.setText(String.valueOf(datos[7]));
                    text10.setText(String.valueOf(datos[8]));
                    text7.setText("-");

                } else if (items[position].equalsIgnoreCase("Últimos 14 días")){
                    franjaTemporal=-14;
                    diaomes=0;
                    try {
                        datos=franjaTemporal(franjaTemporal,diaomes);
                        editarpreferencias();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    text1.setText(String.valueOf(datos[0]));
                    text2.setText(String.valueOf(datos[1]));
                    text3.setText(String.valueOf(datos[2]));
                    text4.setText(String.valueOf(datos[3]));
                    text5.setText(String.valueOf(datos[4]));
                    text6.setText(String.valueOf(datos[5]));
                    text8.setText(String.valueOf(datos[6]));
                    text9.setText(String.valueOf(datos[7]));
                    text10.setText(String.valueOf(datos[8]));
                    text7.setText("-");
                } else if (items[position].equalsIgnoreCase("Último mes")){
                    franjaTemporal=-1;
                    diaomes=1;
                    try {
                        datos=franjaTemporal(franjaTemporal,diaomes);
                        editarpreferencias();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    text1.setText(String.valueOf(datos[0]));
                    text2.setText(String.valueOf(datos[1]));
                    text3.setText(String.valueOf(datos[2]));
                    text4.setText(String.valueOf(datos[3]));
                    text5.setText(String.valueOf(datos[4]));
                    text6.setText(String.valueOf(datos[5]));
                    text8.setText(String.valueOf(datos[6]));
                    text9.setText(String.valueOf(datos[7]));
                    text10.setText(String.valueOf(datos[8]));
                    hemoglobina=calculaHbA1c();
                    text7.setText(String.format("%.2f", hemoglobina));
                } else if (items[position].equalsIgnoreCase("Últimos 3 meses")){
                    franjaTemporal=-3;
                    diaomes=1;
                    try {
                        datos=franjaTemporal(franjaTemporal,diaomes);
                        editarpreferencias();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    text1.setText(String.valueOf(datos[0]));
                    text2.setText(String.valueOf(datos[1]));
                    text3.setText(String.valueOf(datos[2]));
                    text4.setText(String.valueOf(datos[3]));
                    text5.setText(String.valueOf(datos[4]));
                    text6.setText(String.valueOf(datos[5]));
                    text8.setText(String.valueOf(datos[6]));
                    text9.setText(String.valueOf(datos[7]));
                    text10.setText(String.valueOf(datos[8]));
                    hemoglobina=calculaHbA1c();
                    text7.setText(String.format("%.2f", hemoglobina));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner2 = (Spinner) view.findViewById(R.id.spinnerEstadisticasGlucosa2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(itemSpinner2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                momentodeldia=items2[position];
                try {
                    datos=franjaTemporal(franjaTemporal,diaomes);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                text4.setText(String.valueOf(datos[3]));
                text5.setText(String.valueOf(datos[4]));
                text6.setText(String.valueOf(datos[5]));
                text8.setText(String.valueOf(datos[6]));
                text9.setText(String.valueOf(datos[7]));
                text10.setText(String.valueOf(datos[8]));
                editarpreferencias();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    private double calculaHbA1c() {
        double HbA1c;
        HbA1c= (Double.valueOf(text1.getText().toString())+46.7)/28.7;
        return HbA1c;
    }

    private void editarpreferencias() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("ItemSpinner", spinner.getSelectedItemPosition());
        editor.putInt("ItemSpinner2", spinner2.getSelectedItemPosition());
        editor.putString("Momento", momentodeldia);
        editor.apply();
    }

    private int[] franjaTemporal(int tiempo, int diaOmes) throws ParseException {
        int i = 0, j = 0, k=0;
        int mediaGlucosas = 0, glucosaMayor = 0, glucosa = 0, glucosaMenor = Integer.MAX_VALUE;
        int mediaGlucosas2 = 0, glucosaMayor2 = 0, glucosaMenor2 = Integer.MAX_VALUE;
        int mediaGlucosas3 = 0, glucosaMayor3 = 0, glucosaMenor3 = Integer.MAX_VALUE;
        Calendar cal2;
        String fechayhora = "", fecha, hora, momento;
        int[] datos = new int[9];
        Cursor cursor = managerGlucosa.cargarCursorGlucosa();
        if (cursor.moveToFirst()) {
            do {
                cal2 = Calendar.getInstance();
                fecha = cursor.getString(1);
                hora = cursor.getString(2);
                String antesOdespues = cursor.getString(3);
                momento = cursor.getString(4);
                fechayhora = fecha + " " + hora;
                glucosa = Integer.valueOf(cursor.getString(5));

                cal.setTime(sdf.parse(fechayhora));

                if (diaOmes == 0)
                    cal2.add(Calendar.DAY_OF_MONTH, tiempo);
                if (diaOmes == 1)
                    cal2.add(Calendar.MONTH, tiempo);

                if (cal2.before(cal) || (cal.get(Calendar.YEAR) == cal2.get(cal2.YEAR) && cal.get(cal.MONTH) == cal2.get(cal2.MONTH) && cal.get(cal.DAY_OF_MONTH) == cal2.get(cal2.DAY_OF_MONTH))) {
                    sumaGlucosas += glucosa;
                    if (glucosa > glucosaMayor) {
                        glucosaMayor = glucosa;
                    }
                    if (glucosa < glucosaMenor) {
                        glucosaMenor = glucosa;
                    }
                    if (momentodeldia.equalsIgnoreCase(momento)) {
                        if (antesOdespues.equalsIgnoreCase("antes")){
                            sumaGlucosas3 += glucosa;
                            if (glucosa > glucosaMayor3) {
                                glucosaMayor3 = glucosa;
                            }
                            if (glucosa < glucosaMenor3) {
                                glucosaMenor3 = glucosa;
                            }
                            k++;
                        }else {
                            sumaGlucosas2 += glucosa;
                            if (glucosa > glucosaMayor2) {
                                glucosaMayor2 = glucosa;
                            }
                            if (glucosa < glucosaMenor2) {
                                glucosaMenor2 = glucosa;
                            }
                            j++;
                        }
                    }
                    i++;
                }
            } while (cursor.moveToNext());
        cursor.close();
        }
        if(i>0) {
            mediaGlucosas = sumaGlucosas / i;
            datos[0] = mediaGlucosas;
            datos[1] = glucosaMayor;
            datos[2] = glucosaMenor;
            if(j>0) {
                mediaGlucosas2 = sumaGlucosas2 / j;
                datos[3] = mediaGlucosas2;
                datos[4] = glucosaMenor2;
                datos[5] = glucosaMayor2;
            }
            if(k>0){
                mediaGlucosas3 = sumaGlucosas3 / k;
                datos[6] = mediaGlucosas3;
                datos[7] = glucosaMenor3;
                datos[8] = glucosaMayor3;
            }
        }

        sumaGlucosas = 0;
        sumaGlucosas2 = 0;
        sumaGlucosas3 = 0;
        return datos;
    }
}
