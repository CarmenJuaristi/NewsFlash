package com.juaristi.carmen.newsflash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView crearCuentaTextView;
    private ApiServiceUser apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializamos las vistas
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.iniciar_sesion);
        crearCuentaTextView = findViewById(R.id.crear_cuenta);

        // Inicializamos el ApiServiceUser
        apiService = RetrofitClient.getClient().create(ApiServiceUser.class);

        // Configuramos el onClickListener para el botón de inicio de sesión
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Verificamos que los campos no estén vacíos
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, ingresa tu usuario y contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Usamos un Map para pasar los parámetros al cuerpo de la solicitud
                Map<String, String> loginData = new HashMap<>();
                loginData.put("username", username);
                loginData.put("password", password);

                // Llamamos al método loginUser de la API pasando el Map
                Call<LoginResponse> call = apiService.loginUser(loginData);

                // Ejecutamos la llamada de forma asíncrona
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            // Si la respuesta es exitosa
                            Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            String token = response.body().getToken();
                            // Aquí podrías guardar el token en SharedPreferences si lo necesitas
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish(); // Cierra la actividad de login
                        } else {
                            // Si ocurre un error con la respuesta
                            Toast.makeText(LoginActivity.this, "Error al iniciar sesión: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        // Manejo de error de la solicitud
                        Toast.makeText(LoginActivity.this, "Error al iniciar sesión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Configurar onClickListener para el texto "Crear cuenta"
        crearCuentaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}
