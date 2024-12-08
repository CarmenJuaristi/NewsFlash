package com.juaristi.carmen.newsflash;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FavouritesFragment extends Fragment {

    private NewsViewModel newsViewModel; // ViewModel para manejar datos de noticias
    private NewsAdapter newsAdapter; // Adaptador para mostrar artículos en RecyclerView
    private RecyclerView recyclerView; // RecyclerView para la lista de favoritos

    public FavouritesFragment() {
        super(R.layout.fragment_favourites); // Vincula este fragmento al diseño correspondiente
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa el RecyclerView
        recyclerView = view.findViewById(R.id.recyclerFavourites);

        // Obtén el ViewModel desde la actividad principal
        newsViewModel = ((NewsActivity) requireActivity()).getNewsViewModel();

        // Configura el RecyclerView
        setupFavouritesRecycler();

        // Configura el click en los elementos del adaptador
        newsAdapter.setOnItemClickListener(article -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("article", article); // Asegúrate de que `Article` implemente Serializable
            ((NewsActivity) requireActivity()).getNavController()
                    .navigate(R.id.action_favouritesFragment_to_articleFragment, bundle);
        });

        // Configuración para Swipe (arrastrar para eliminar)
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true; // Permitir movimiento, aunque no se realiza ninguna acción en este caso
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                Article article = newsAdapter.getDiffer().getCurrentList().get(position);
                newsViewModel.deleteArticle(article); // Elimina el artículo de favoritos

                // Muestra Snackbar con opción de deshacer
                Snackbar.make(view, "Removed from favourites", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> newsViewModel.addToFavourites(article))
                        .show();
            }
        };

        // Adjunta el ItemTouchHelper al RecyclerView
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        // Observa los cambios en la lista de favoritos
        newsViewModel.getFavouritesNews().observe(getViewLifecycleOwner(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                newsAdapter.getDiffer().submitList(articles); // Actualiza la lista del adaptador
            }
        });
    }

    private void setupFavouritesRecycler() {
        newsAdapter = new NewsAdapter(); // Inicializa el adaptador
        recyclerView.setAdapter(newsAdapter); // Configura el adaptador para el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // Establece
