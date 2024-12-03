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
        Fragment myHome = new Fragment();
        Fragment myCategories = new Fragment();
        Fragment myTrend = new Fragment();
        Fragment myProfile = new Fragment();

        BottomNavigationView bar = findViewById(R.id.bottomNavigation);

        bar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.category){
                    Fragment Categories = new Fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,Categories).commit();
                }
                if(item.getItemId()==R.id.home){
                    Fragment HomeActivity = new Fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,HomeActivity).commit();
                }
                if(item.getItemId()==R.id.trend){
                    Fragment TrendActivity = new Fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,TrendActivity).commit();
                }
                if(item.getItemId()==R.id.profile){
                    Fragment Profile = new Fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,Profile).commit();
                }
                return true;
            }
        });
    }
    private Fragment createCategoryFragment(String category) {
        Categories categoriesFragment = new Categories();
        Bundle args = new Bundle();
        args.putString("category", category);
        categoriesFragment.setArguments(args);
        return categoriesFragment;
    }
}