package com.juaristi.carmen.newsflash;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity   {

    private Context context = this;

    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment myInicio = new Fragment();
        Fragment myCategorias = new Fragment();
        Fragment myTendencia = new Fragment();
        Fragment myPerfil = new Fragment();

        BottomNavigationView bar = findViewById(R.id.bottomNavigation);

        bar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.categoria){
                    Fragment Categories = new Fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,Categories).commit();
                }
                if(item.getItemId()==R.id.inicio){
                    Fragment HomeActivity = new Fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,HomeActivity).commit();
                }
                if(item.getItemId()==R.id.tendencia){
                    Fragment Tendencia = new Fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,Tendencia).commit();
                }
                if(item.getItemId()==R.id.perfil){
                    Fragment Perfil = new Fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,Perfil).commit();
                }
                return true;
            }
        });
    }
    private Fragment createCategoryFragment(String category) {
        Categories categoriasFragment = new Categories();
        Bundle args = new Bundle();
        args.putString("category", category);
        categoriasFragment.setArguments(args);
        return categoriasFragment;
    }
}