package com.juaristi.carmen.newsflash;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Categories extends Fragment {
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCategorias); // Asegúrate de usar el ID correcto
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lista de categorías
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Technology"));
        categories.add(new Category("Business"));
        categories.add(new Category("Health"));
        categories.add(new Category("Sports"));

        // Configurar el adaptador y manejar el clic en la categoría
        CategoryAdapter adapter = new CategoryAdapter(categories, categoryName -> {
            Intent intent = new Intent(getContext(), HomeActivity.class);
            intent.putExtra("category", categoryName);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        return view;
    }
}


