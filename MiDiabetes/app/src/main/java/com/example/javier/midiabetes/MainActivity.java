package com.example.javier.midiabetes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navview;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        navview = (NavigationView) findViewById(R.id.nav_view);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences prefs = getSharedPreferences("Usuario", MODE_PRIVATE);

        if(prefs.getInt("return", 0)==0) {
            navview.getMenu().getItem(0).setChecked(true);
            replaceFragment(new InicioFragment());
        }else if(prefs.getInt("return", 0)==1) {
            navview.getMenu().getItem(1).setChecked(true);
            replaceFragment(new MenuGlucosaFragment());
        }else if(prefs.getInt("return", 0)==2) {
            navview.getMenu().getItem(2).setChecked(true);
            replaceFragment(new MenuAlimentosFragment());
        }else if(prefs.getInt("return", 0)==3) {
            navview.getMenu().getItem(3).setChecked(true);
        replaceFragment(new MenuUsuarioFragment());
    }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        TextView textNombre = (TextView) header.findViewById(R.id.textViewNombre);
        TextView textEmail = (TextView) header.findViewById(R.id.textViewEmail);

        textNombre.setText(prefs.getString("nombre", ""));
        textEmail.setText(prefs.getString("email", ""));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            replaceFragment(new MenuUsuarioFragment());
        }else if (id == R.id.nav_inicio){
            replaceFragment(new InicioFragment());
        } else if (id == R.id.nav_glucosa) {
            replaceFragment(new MenuGlucosaFragment());
        } else if (id == R.id.nav_comidas){
            replaceFragment(new MenuAlimentosFragment());
        }else if (id == R.id.nav_hemoglobina){
            replaceFragment(new GraficaHbA1cFragment());
        }else if (id == R.id.nav_exportar){
            replaceFragment(new ExportarPDFActivity());
    }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.containerPrincipal, fragment);

        transaction.commit();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(navview.getMenu().getItem(0).isChecked()){
                finish();
                return true;
            }else{
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("return", 0);
                navview.getMenu().getItem(0).setChecked(true);
                replaceFragment(new InicioFragment());
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
