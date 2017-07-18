package com.example.javier.midiabetes;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class ListDataAdapter extends BaseAdapter{
    protected Activity activity;
    protected ArrayList<ListData> items;

    public ListDataAdapter(Activity activity, ArrayList<ListData> items) {
        this.activity = activity;
        this.items = items;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi=view;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.activity_elemento_lista, null);
        }

        ListData item = items.get(i);

        RelativeLayout layout =(RelativeLayout) vi.findViewById(R.id.activity_elemento_lista);
        TextView fecha = (TextView) vi.findViewById(R.id.txtDate);
        TextView hora = (TextView) vi.findViewById(R.id.txtHour);
        TextView txtGlu = (TextView) vi.findViewById(R.id.txtGlu);
        TextView glucosa = (TextView) vi.findViewById(R.id.txtGlucosa);
        TextView momento = (TextView) vi.findViewById(R.id.txtmomento);
        TextView antesodespues = (TextView) vi.findViewById(R.id.txtAntesDespues);
        TextView txtD = (TextView) vi.findViewById(R.id.txtDe);

        TextView comida = (TextView) vi.findViewById(R.id.txtComidaLista);
        TextView txtHi = (TextView) vi.findViewById(R.id.textViewHidratos);
        TextView hidratos = (TextView) vi.findViewById(R.id.txtHidratosLista);
        TextView txtRa = (TextView) vi.findViewById(R.id.textViewRaciones);
        TextView raciones = (TextView) vi.findViewById(R.id.txtRacionesLista);
        TextView date = (TextView) vi.findViewById(R.id.txtDateComidas);

        RelativeLayout layout2 = (RelativeLayout) vi.findViewById(R.id.layaoutlista2);
        TextView comida2 = (TextView) vi.findViewById(R.id.txtComida2);
        TextView hidratos2 = (TextView) vi.findViewById(R.id.txtHidratos2);
        TextView raciones2 = (TextView) vi.findViewById(R.id.txtRaciones2);

        date.setText(item.getFecha2());

        if(item.getTipo()==1){
            layout.setBackgroundResource(R.drawable.elementolista);
            fecha.setVisibility(View.VISIBLE);
            fecha.setText(item.getFecha());
            hora.setVisibility(View.VISIBLE);
            hora.setText(item.getHora());
            glucosa.setVisibility(View.VISIBLE);
            txtGlu.setVisibility(View.VISIBLE);
            glucosa.setText(item.getGlucosa());
            momento.setVisibility(View.VISIBLE);
            momento.setText(item.getMomento());
            hora.setVisibility(View.VISIBLE);
            txtD.setVisibility(View.VISIBLE);
            antesodespues.setText(item.getAntesodespues());
            antesodespues.setVisibility(View.VISIBLE);

            layout2.setVisibility(View.GONE);
            comida.setVisibility(View.GONE);
            hidratos.setVisibility(View.GONE);
            raciones.setVisibility(View.GONE);
            date.setVisibility(View.GONE);
            txtRa.setVisibility(View.GONE);
            txtHi.setVisibility(View.GONE);

            String valorMomento = momento.getText().toString();
            int valorGlucosa = Integer.valueOf(glucosa.getText().toString());

            DataBaseManagerDatosUsuario managerUsuario = new DataBaseManagerDatosUsuario(vi.getContext());
            Cursor cursor = managerUsuario.cargarCursorDatosUsuario();
            if (cursor.moveToFirst()) {
                do {
                 if(((valorMomento.equalsIgnoreCase("desayuno") || valorMomento.equalsIgnoreCase("almuerzo") || valorMomento.equalsIgnoreCase("comida") || valorMomento.equalsIgnoreCase("merienda")
                         || valorMomento.equalsIgnoreCase("cena")) && antesodespues.getText().toString().length()>1) &&
                         ((cursor.getString(5).length()>1 && cursor.getString(8).length()>1))){
                     if(antesodespues.getText().toString().equalsIgnoreCase("antes")){
                         if(valorGlucosa<=Integer.valueOf(cursor.getString(8))+10 || valorGlucosa>=Integer.valueOf(cursor.getString(5))-10){
                             layout.setBackgroundResource(R.drawable.elementolistaamarillo);
                             if(valorGlucosa<Integer.valueOf(cursor.getString(8))-5 || valorGlucosa>Integer.valueOf(cursor.getString(5))+20){
                                 layout.setBackgroundResource(R.drawable.elementolistanaranja);
                                 if(valorGlucosa<Integer.valueOf(cursor.getString(8))-15 || valorGlucosa>Integer.valueOf(cursor.getString(5))+50){
                                     layout.setBackgroundResource(R.drawable.elementolistarojo);
                                 }
                             }
                         }else{
                             layout.setBackgroundResource(R.drawable.elementolistaverde);
                         }
                     }else if (cursor.getString(9).length()>1 && cursor.getString(6).length()>1){
                         if(valorGlucosa<=Integer.valueOf(cursor.getString(9))+10 || valorGlucosa>=Integer.valueOf(cursor.getString(6))-10){
                             layout.setBackgroundResource(R.drawable.elementolistaamarillo);
                             if(valorGlucosa<=Integer.valueOf(cursor.getString(9))-5 || valorGlucosa>Integer.valueOf(cursor.getString(6))+20){
                                 layout.setBackgroundResource(R.drawable.elementolistanaranja);
                                 if(valorGlucosa<=Integer.valueOf(cursor.getString(9))-15 || valorGlucosa>Integer.valueOf(cursor.getString(6))+50){
                                     layout.setBackgroundResource(R.drawable.elementolistarojo);
                                 }
                             }
                         }else{
                             layout.setBackgroundResource(R.drawable.elementolistaverde);
                         }
                     }
                 }else if((valorMomento.equalsIgnoreCase("ejecicio") || valorMomento.equalsIgnoreCase("otro")) && (cursor.getString(11).length()>1 && cursor.getString(12).length()>1)){
                     if(valorGlucosa<=Integer.valueOf(cursor.getString(11))+10 || valorGlucosa>=Integer.valueOf(cursor.getString(12))-10){
                         layout.setBackgroundResource(R.drawable.elementolistaamarillo);
                         if(valorGlucosa<Integer.valueOf(cursor.getString(11))-5 || valorGlucosa>Integer.valueOf(cursor.getString(12))+20){
                             layout.setBackgroundResource(R.drawable.elementolistanaranja);
                             if(valorGlucosa<Integer.valueOf(cursor.getString(11))-15 || valorGlucosa>Integer.valueOf(cursor.getString(12))+50){
                                 layout.setBackgroundResource(R.drawable.elementolistarojo);
                             }
                         }
                     }else{
                         layout.setBackgroundResource(R.drawable.elementolistaverde);
                     }
                 }else if(valorMomento.equalsIgnoreCase("control nocturno")&& (cursor.getString(10).length()>1 && cursor.getString(7).length()>1)) {
                     if ( valorGlucosa <= Integer.valueOf(cursor.getString(10))+10 || valorGlucosa >= Integer.valueOf(cursor.getString(7))-10) {
                         layout.setBackgroundResource(R.drawable.elementolistaamarillo);
                         if (valorGlucosa < Integer.valueOf(cursor.getString(10)) - 5 || valorGlucosa > Integer.valueOf(cursor.getString(7)) + 20) {
                             layout.setBackgroundResource(R.drawable.elementolistanaranja);
                             if (valorGlucosa < Integer.valueOf(cursor.getString(10)) - 15 || valorGlucosa > Integer.valueOf(cursor.getString(7)) +50) {
                                 layout.setBackgroundResource(R.drawable.elementolistarojo);
                             }
                         }
                     } else {
                         layout.setBackgroundResource(R.drawable.elementolistaverde);
                     }
                 }else{
                     layout.setBackgroundResource(R.drawable.elementolista);
                 }
                } while (cursor.moveToNext());
                cursor.close();
            }

        }else if(item.getTipo()==2){
            layout.setBackgroundResource(R.drawable.elementolista2);
            comida.setVisibility(View.VISIBLE);
            comida.setText(item.getComida());
            hidratos.setVisibility(View.VISIBLE);
            txtHi.setVisibility(View.VISIBLE);
            hidratos.setText(item.getHidratos());
            raciones.setVisibility(View.VISIBLE);
            txtRa.setVisibility(View.VISIBLE);
            raciones.setText(item.getRaciones());
            date.setVisibility(View.VISIBLE);
            date.setText(item.getFecha2());

            layout2.setVisibility(View.GONE);
            fecha.setVisibility(View.GONE);
            hora.setVisibility(View.GONE);
            glucosa.setVisibility(View.GONE);
            txtGlu.setVisibility(View.GONE);
            momento.setVisibility(View.GONE);
            txtD.setVisibility(View.GONE);
            antesodespues.setVisibility(View.GONE);

        }else{
            layout.setBackgroundResource(R.drawable.elementolista2pequena);
            layout2.setVisibility(View.VISIBLE);
            comida2.setText(item.getComida());
            hidratos2.setText(item.getHidratos());
            raciones2.setText(item.getRaciones());

            fecha.setVisibility(View.GONE);
            hora.setVisibility(View.GONE);
            glucosa.setVisibility(View.GONE);
            txtGlu.setVisibility(View.GONE);
            momento.setVisibility(View.GONE);
            comida.setVisibility(View.GONE);
            hidratos.setVisibility(View.GONE);
            raciones.setVisibility(View.GONE);
            date.setVisibility(View.GONE);
            txtRa.setVisibility(View.GONE);
            txtHi.setVisibility(View.GONE);
            txtD.setVisibility(View.GONE);
            antesodespues.setVisibility(View.GONE);
        }
        return vi;
    }
}
