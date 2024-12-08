package com.juaristi.carmen.newsflash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, createAccountButton;
    private ApiServiceUser apiServiceUser;  // Asegúrate de usar la interfaz correcta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Asegúrate de usar el layout correcto

        // Vincula los elementos del XML a las variables de la actividad
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.iniciar_sesion);
        createAccountButton = findViewById(R.id.crear_cuenta);

        // Usa el cliente Retrofit para la API de usuarios
        apiServiceUser = RetrofitClient.getUserClient().create(ApiServiceUser.class);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener valores de los EditTexts
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Crear el objeto LoginRequest
                LoginRequest loginRequest = new LoginRequest(username, password);

                // Llamar al método loginUser del servicio
                Call<LoginResponse> call = apiServiceUser.loginUser(loginRequest);

                // Manejar la respuesta de la llamada asíncrona usando Retrofit
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            // Manejar la respuesta exitosa aquí
                            Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            // Aquí podrías guardar el token de sesión en SharedPreferences o similar
                            // Por ejemplo: saveToken(response.body().getToken());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish(); // Cerrar la actividad de inicio de sesión
                        } else {
                            // Manejar errores de la respuesta
                            // Por ejemplo, mostrar un mensaje de error basado en el código de respuesta HTTP
                            Toast.makeText(LoginActivity.this, "Error al iniciar sesión: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        // Manejar errores de la solicitud
                        Toast.makeText(LoginActivity.this, "Error al iniciar sesión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Configurar onClickListener para el texto de "¿No tienes cuenta?"
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad de registro (puedes implementar esta parte según tu flujo de navegación)
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}

