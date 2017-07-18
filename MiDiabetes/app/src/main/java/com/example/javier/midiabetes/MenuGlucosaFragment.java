package com.example.javier.midiabetes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MenuGlucosaFragment extends Fragment{

    private boolean firstTime = true;
    private SharedPreferences prefs;
    private boolean reload = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_glucosa, container, false);

        FloatingActionButton btnagregarDatos = (FloatingActionButton) view.findViewById(R.id.btnAgregar);
        btnagregarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(getContext(), AgregarGlucosaActivity.class);
                    startActivity(i);
            }
        });

        if(firstTime){
            firstTime=false;
            replaceFragment(new ListaGlucosaFragment());
        }

        prefs = getContext().getSharedPreferences("Usuario", Context.MODE_PRIVATE);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    replaceFragment(new ListaGlucosaFragment());
                } else if (tab.getPosition() == 1) {
                    replaceFragment(new EstadisticasGlucosaFragment());
                } else {
                    replaceFragment(new GraficaGlucosaFragment());
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return  view;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        transaction.commit();
    }

    public void onResume() {
        super.onResume();
        if(reload) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("return", 1);
            editor.apply();
            getActivity().finish();
            startActivity(getActivity().getIntent());
        }
    }

    public void onStop() {
        super.onStop();
        reload = true;
    }
}
