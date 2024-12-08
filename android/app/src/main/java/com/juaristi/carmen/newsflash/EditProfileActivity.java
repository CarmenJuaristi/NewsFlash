package com.juaristi.carmen.newsflash;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText;
    private ApiServiceUser apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        usernameEditText = findViewById(R.id.editTextUsername);
        emailEditText = findViewById(R.id.editTextEmail);
        Button saveButton = findViewById(R.id.btn_save_changes);
        Button cancelButton = findViewById(R.id.btn_cancel_changes);

        // Obtener el cliente de Retrofit para usuarios
        apiService = RetrofitClient.getUserClient().create(ApiServiceUser.class);

        // Cargar datos del usuario
        loadUserData();

        saveButton.setOnClickListener(v -> saveChanges());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void loadUserData() {
        Call<UserResponse> call = apiService.getUserData(); // Ajusta el endpoint según tu API
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse user = response.body();
                    usernameEditText.setText(user.getUsername());
                    emailEditText.setText(user.getEmail());
                } else {
                    Toast.makeText(EditProfileActivity.this, "Error loading user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveChanges() {
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (username.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> updates = new HashMap<>();
        updates.put("username", username);
        updates.put("email", email);

        Call<Void> call = apiService.updateUser(updates); // Ajusta el endpoint según tu API
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
