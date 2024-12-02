package com.juaristi.carmen.newsflash;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ImageView logo;
    private TextView titulo;
    private TextView email_texto;
    private EditText email;
    private TextView username_text;
    private EditText username;
    private TextView password_text;
    private EditText password;
    private Button crear_cuenta;
    private ApiServiceUser apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializamos las vistas
        logo = findViewById(R.id.logo);
        titulo = findViewById(R.id.Bienvenida);
        email_texto = findViewById(R.id.email_text);
        email = findViewById(R.id.correo);
        username_text = findViewById(R.id.username_text);
        username = findViewById(R.id.username);
        password_text = findViewById(R.id.password_text);
        password = findViewById(R.id.password);
        crear_cuenta = findViewById(R.id.crear_cuenta);

        // Inicializamos el ApiServiceUser
        apiService = RetrofitClient.getClient().create(ApiServiceUser.class);

        // Configuramos el botón de registro
        crear_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPostRegister();
            }
        });
    }

    private void sendPostRegister() {
        // Obtener los valores de los EditTexts
        String usernameStr = username.getText().toString();
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();

        // Validar que los campos no estén vacíos
        if (usernameStr.isEmpty() || emailStr.isEmpty() || passwordStr.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el Map con los datos del usuario
        Map<String, String> userData = new HashMap<>();
        userData.put("username", usernameStr);
        userData.put("email", emailStr);
        userData.put("password", passwordStr);

        // Llamar al método createUser pasando el Map
        Call<UserResponse> call = apiService.createUser(userData);

        // Manejar la respuesta de la llamada asíncrona usando Retrofit
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    // Si la respuesta es exitosa
                    Toast.makeText(RegisterActivity.this, "Usuario creado con éxito", Toast.LENGTH_SHORT).show();
                    finish(); // Cerrar la actividad después de un registro exitoso
                } else {
                    // Si ocurre un error con la respuesta
                    Toast.makeText(RegisterActivity.this, "Error al crear usuario: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Manejo de errores de la solicitud
                Toast.makeText(RegisterActivity.this, "Error al crear usuario: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
