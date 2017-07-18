package com.example.javier.midiabetes;

import android.content.Context;
import android.database.Cursor;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;


public class MyMarkerView extends MarkerView {
    private TextView tvContent, tvContent2, tvContent3;
    private String fecha, hora;

    public MyMarkerView(Context context, int layoutResource, Entry e) {
        super(context, layoutResource);

        // find your layout components
        tvContent = (TextView) findViewById(R.id.textGlucosaGrafica);
        tvContent2 = (TextView) findViewById(R.id.textFechaGrafica);
        tvContent3 = (TextView) findViewById(R.id.textHoraGrafica);

        DataBaseManagerGlucosa managerGlucosa = new DataBaseManagerGlucosa(getContext());
        Cursor cursor = managerGlucosa.cargarCursorGlucosa();

        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                if(i==e.getX()){
                    fecha=cursor.getString(1);
                    hora=cursor.getString(2);
                }
                i++;
            }  while (cursor.moveToNext());
            cursor.close();
        }
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        tvContent.setText("" + e.getY());
        tvContent2.setText(fecha);
        tvContent3.setText(hora);

        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}