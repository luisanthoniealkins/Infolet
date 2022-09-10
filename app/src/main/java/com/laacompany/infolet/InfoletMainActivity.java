package com.laacompany.infolet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

public class InfoletMainActivity extends AppCompatActivity {

    private DrawerLayout drawer;

    private static final String Tag = "infoletTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(Tag,"create");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.id_infolet_navigation_menu_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfoletMainFragment()).commit();
                        break;
                    case R.id.id_infolet_navigation_menu_wallet:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfoletWalletFragment()).commit();
                        break;
                    case R.id.id_infolet_navigation_menu_transaction:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfoletTransactionListFragment()).commit();
                        break;
                    case R.id.id_infolet_navigation_menu_money_report:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfoletReportListFragment()).commit();
                        break;
                    case R.id.id_infolet_navigation_menu_graph:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfoletGraphFragment()).commit();
                        break;
                    case R.id.id_infolet_navigation_menu_setting:
                        break;
                    case R.id.id_infolet_navigation_menu_send:
                        break;
                    case R.id.id_infolet_navigation_menu_share:
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfoletMainFragment()).commit();
            navigationView.setCheckedItem(R.id.id_infolet_navigation_menu_home);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Tag,"resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Tag,"pause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(Tag,"start");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(Tag,"stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Tag,"destroy");
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setCancelable(true);
            builder.setTitle("Exit Application");
            builder.setMessage("Are you sure you want to exit?");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }


}



