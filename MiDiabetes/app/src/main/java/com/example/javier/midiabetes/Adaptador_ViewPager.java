package com.example.javier.midiabetes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Javier on 24/11/2016.
 */

public class Adaptador_ViewPager extends FragmentPagerAdapter {

    // en esta variable almacenaremos el nº de secciones
    int numeroDeSecciones=3;

    public Adaptador_ViewPager(FragmentManager fm) {
        super(fm);
        this.numeroDeSecciones=numeroDeSecciones;
    }

    @Override
    public Fragment getItem(int position) {
        // recibimos la posición por parámetro y en función de ella devolvemos el Fragment correspondiente a esa sección.
        switch (position) {
            case 0:
                return new GlucosaFragment();

            case 1:
                return new EstadisticasFragment();

            case 2:
                return new GraficasFragment();

            // si la posición recibida no se corresponde a ninguna sección
            default:
                return null;
        }
    }

    /*  getCount() devuelve el nº de secciones, dato que recibiremos cuando instanciemos el adaptador
        desde nuestra actividad principal */
    public int getCount(){
        return numeroDeSecciones;
    }


}