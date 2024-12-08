package com.juaristi.carmen.newsflash;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        logo = findViewById(R.id.logo);
        email_texto = findViewById(R.id.email_text);
        email = findViewById(R.id.correo);
        username_text = findViewById(R.id.username_text);
        username = findViewById(R.id.username);
        password_text = findViewById(R.id.password_text);
        password = findViewById(R.id.password);
        crear_cuenta = findViewById(R.id.crear_cuenta);

        // Cambia 'getClient' por 'getUserClient' para usar el cliente adecuado
        apiService = RetrofitClient.getUserClient().create(ApiServiceUser.class);

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

        // Crear el objeto UserRequest
        UserRequest userRequest = new UserRequest(usernameStr, emailStr, passwordStr);

        // Llamar al método createUser del servicio
        Call<UserResponse> call = apiService.createUser(userRequest);

        // Manejar la respuesta de la llamada asíncrona usando Retrofit
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    // Manejar la respuesta exitosa aquí
                    Toast.makeText(RegisterActivity.this, "Usuario creado", Toast.LENGTH_SHORT).show();
                    finish(); // Cerrar la actividad después de un registro exitoso
                } else {
                    // Manejar errores de la respuesta
                    // Por ejemplo, mostrar un mensaje de error basado en el código de respuesta HTTP
                    Toast.makeText(RegisterActivity.this, "Error al crear usuario: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Manejar errores de la solicitud
                Toast.makeText(RegisterActivity.this, "Error al crear usuario: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
