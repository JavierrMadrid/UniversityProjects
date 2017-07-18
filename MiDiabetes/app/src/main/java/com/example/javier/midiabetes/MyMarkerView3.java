package com.example.javier.midiabetes;

import android.content.Context;
import android.database.Cursor;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;


public class MyMarkerView3 extends MarkerView {
    private TextView tvContent, tvContent2;
    private String fecha;

    public MyMarkerView3(Context context, int layoutResource, Entry e) {
        super(context, layoutResource);

        tvContent = (TextView) findViewById(R.id.textHidratosGrafica);
        tvContent2 = (TextView) findViewById(R.id.textFechaGrafica2);

        DataBaseManagerHemoglobina managerHemoglobina = new DataBaseManagerHemoglobina(getContext());
        Cursor cursor = managerHemoglobina.cargarCursorHemoglobina();

        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                if(i==e.getX()){
                    fecha=cursor.getString(2);
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

        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}