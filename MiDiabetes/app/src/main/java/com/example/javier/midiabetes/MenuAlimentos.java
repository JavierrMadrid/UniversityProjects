package com.example.javier.midiabetes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MenuAlimentos extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    private TabLayout tabLayout;
    private boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_alimentos);

        tabLayout = (TabLayout) findViewById(R.id.tabs2);
        tabLayout.setOnTabSelectedListener(this);

        if(firstTime){
            firstTime=false;
            replaceFragment(new ListaAlimentosFragment());
        }

    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            replaceFragment(new ListaAlimentosFragment());
        } else if (tab.getPosition() == 1) {
            replaceFragment(new Estadisticas2Fragment());
        } else {
            replaceFragment(new ListaAlimentosFragment());
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container2, fragment);

        transaction.commit();
    }
}
