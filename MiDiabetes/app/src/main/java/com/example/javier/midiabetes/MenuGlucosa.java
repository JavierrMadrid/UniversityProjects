package com.example.javier.midiabetes;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuGlucosa extends AppCompatActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_glucosa);

        FloatingActionButton btnagregarDatos = (FloatingActionButton) findViewById(R.id.btnAgregar);
        btnagregarDatos.setOnClickListener(this);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setOnTabSelectedListener(this);

        if(firstTime){
            firstTime=false;
            replaceFragment(new GlucosaFragment());
        }

    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            replaceFragment(new GlucosaFragment());
        } else if (tab.getPosition() == 1) {
            replaceFragment(new EstadisticasFragment());
        } else {
            replaceFragment(new GraficasFragment());
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    public void onClick(View boton_pulsado) {
        if(boton_pulsado.getId()== R.id.btnAgregar){
            Intent i = new Intent(this, AgregarGlucosa.class);
            finish();
            startActivity(i);
        }
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        transaction.commit();
    }
}
