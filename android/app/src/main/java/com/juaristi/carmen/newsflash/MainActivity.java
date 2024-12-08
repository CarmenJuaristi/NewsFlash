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
        Fragment myArticles = new Fragment();
        Fragment myFavourites = new Fragment();
        Fragment mySearch = new Fragment();
        Fragment myProfile = new Fragment();

        BottomNavigationView bar = findViewById(R.id.bottomNavigation);

        bar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.category){
                    Fragment FavouritesFragment = new Fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,FavouritesFragment).commit();
                }
                if(item.getItemId()==R.id.home){
                    Fragment ArticleFragment = new Fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,ArticleFragment).commit();
                }
                if(item.getItemId()==R.id.trend){
                    Fragment SearchFragment = new Fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,SearchFragment).commit();
                }
                if(item.getItemId()==R.id.profile){
                    Fragment ProfileActivity = new Fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,ProfileActivity).commit();
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