package com.juaristi.carmen.newsflash;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ArticleFragment extends Fragment {

    private NewsViewModel newsViewModel; // ViewModel para manejar los datos relacionados con noticias
    private WebView webView; // WebView para mostrar contenido del artículo
    private FloatingActionButton fab; // Botón flotante para añadir a favoritos

    public ArticleFragment() {
        super(R.layout.fragment_article); // Vincula este fragmento al diseño correspondiente
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa los elementos de la vista
        webView = view.findViewById(R.id.webView); // Asegúrate de que el ID sea el correcto en tu XML
        fab = view.findViewById(R.id.fab); // Asegúrate de que el ID sea el correcto en tu XML

        // Obtén el ViewModel desde la actividad principal
        newsViewModel = ((NewsActivity) requireActivity()).getNewsViewModel();

        // Obtén los argumentos pasados a este fragmento
        Article article = (Article) requireArguments().getSerializable("article"); // Asegúrate de que Article implemente Serializable

        // Configura el WebView para cargar la URL del artículo
        if (article != null && article.getUrl() != null) {
            webView.setWebViewClient(new WebViewClient()); // Establece un WebViewClient para manejar la navegación
            webView.loadUrl(article.getUrl()); // Carga la URL en el WebView
        }

        // Configura el Floating Action Button (fab) para añadir el artículo a favoritos
        fab.setOnClickListener(v -> {
            if (article != null) {
                newsViewModel.addToFavourites(article); // Llama al método del ViewModel para guardar en favoritos
                Snackbar.make(view, "Added to favourites", Snackbar.LENGTH_SHORT).show(); // Muestra un mensaje al usuario
            }
        });
    }
}
