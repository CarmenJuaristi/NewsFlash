package com.juaristi.carmen.newsflash;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;

public class ProfileActivity extends Fragment {

    private ApiServiceUser apiService;
    private ApiServiceFavorites apiFavorites;
    private RecyclerView recyclerViewFavorites;
    private FavoritesAdapter favoritesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        view.findViewById(R.id.editProfile).setOnClickListener(v -> openEditProfile());
        view.findViewById(R.id.deleteAccount).setOnClickListener(v -> confirmAction("Delete Account", "Are you sure you want to delete your account?", this::deleteAccount));
        view.findViewById(R.id.logout).setOnClickListener(v -> confirmAction("Logout", "Are you sure you want to log out?", this::logout));
        view.findViewById(R.id.viewFavorites).setOnClickListener(v -> loadFavorites());

        recyclerViewFavorites = view.findViewById(R.id.recyclerViewFavorites);
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(getContext()));

        // Cambiar getClient() por getUserClient()
        apiService = RetrofitClient.getUserClient().create(ApiServiceUser.class);

        return view;
    }

    private void openEditProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void confirmAction(String title, String message, Runnable action) {
        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Confirm", (dialog, which) -> action.run())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteAccount() {
        // Asegúrate de que el email esté disponible, por ejemplo, usando SharedPreferences
        SharedPreferences preferences = getContext().getSharedPreferences("user_data", MODE_PRIVATE);
        String email = preferences.getString("email", null);  // Recuperamos el email del usuario

        if (email == null) {
            Toast.makeText(getContext(), "Email not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Realizamos la llamada al servicio de eliminación de usuario usando el email
        Call<ApiResponse> call = apiService.deleteUser(email);  // Pasa el email al servicio
        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();  // Obtenemos el cuerpo de la respuesta

                    if (apiResponse != null && apiResponse.isSuccess()) {
                        // Si la respuesta indica éxito, mostramos el mensaje adecuado
                        Toast.makeText(getContext(), "Account deleted", Toast.LENGTH_SHORT).show();
                        goToLogin();  // Redirigir a la pantalla de login después de eliminar la cuenta
                    } else {
                        // Si la API devolvió un mensaje de error
                        Toast.makeText(getContext(), "Error: " + (apiResponse != null ? apiResponse.getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error deleting account", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        // Implement your logout logic
        Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
        goToLogin();
    }

    private void goToLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void loadFavorites() {
        // Asegúrate de que el email esté disponible, por ejemplo, usando SharedPreferences
        SharedPreferences preferences = getContext().getSharedPreferences("user_data", MODE_PRIVATE);
        String email = preferences.getString("email", null);  // Recuperamos el email del usuario

        if (email == null) {
            Toast.makeText(getContext(), "Email not found", Toast.LENGTH_SHORT).show();
            return;
        }
        Call<List<Article>> call = apiFavorites.getFavorites(email); // Ajustar según tu API
        call.enqueue(new retrofit2.Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, retrofit2.Response<List<Article>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    favoritesAdapter = new FavoritesAdapter(response.body(), news -> {
                        Intent intent = new Intent(getContext(), NewsDetailActivity.class);
                        intent.putExtra("news_title", news.getTitle());
                        intent.putExtra("news_summary", news.getSummary());
                        startActivity(intent);
                    });
                    recyclerViewFavorites.setAdapter(favoritesAdapter);
                } else {
                    Toast.makeText(getContext(), "No favorites found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                Toast.makeText(getContext(), "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
