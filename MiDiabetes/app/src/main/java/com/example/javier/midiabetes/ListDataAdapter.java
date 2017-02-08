package com.example.javier.midiabetes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Javier on 21/11/2016.
 */

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

        TextView fecha = (TextView) vi.findViewById(R.id.txtDate);
        fecha.setText(item.getFecha());

        TextView hora = (TextView) vi.findViewById(R.id.txtHour);
        hora.setText(item.getHora());

        TextView glucosa = (TextView) vi.findViewById(R.id.txtGlucosa);
        glucosa.setText(item.getGlucosa());

        //TextView antesodespues = (TextView) vi.findViewById(R.id.txtantesodespues);
        //antesodespues.setText(item.getAntesodespues());

        TextView momento = (TextView) vi.findViewById(R.id.txtmomento);
        momento.setText(item.getMomento());

        return vi;
    }
}
