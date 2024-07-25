package com.example.proyectoaula;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import org.json.JSONException;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.HashMap;
import java.util.Map;


import android.content.Context;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {
    private Button btnRegistrarse, btnIniciarSesion, btnOlvide;
    private EditText txtEmail, txtPassword;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Verificar si la sesión ya está iniciada
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String userRole = sharedPreferences.getString("user_role", null);
        if (userRole != null) {
            // Redirigir a la actividad correspondiente
            redirectToRoleActivity(userRole);
        }

        btnRegistrarse = findViewById(R.id.btnReg);
        btnIniciarSesion = findViewById(R.id.btnIni);
        btnOlvide = findViewById(R.id.btnPass);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPass);

        requestQueue = Volley.newRequestQueue(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnRegistrarse.setOnClickListener(view -> {
            Intent activar = new Intent(MainActivity.this, Registrarse.class);
            startActivity(activar);
        });

        btnIniciarSesion.setOnClickListener(view -> {
            if (validateInput()) {
                iniciarSesion("http://169.254.223.76:8080/PoyectoAula/Login.php");
            }
        });
    }


    private boolean validateInput() {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "El email es requerido", Toast.LENGTH_SHORT).show();
            txtEmail.setBackgroundResource(R.drawable.error_border);
            return false;
        } else {
            txtEmail.setBackgroundResource(0);
        }

        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "La contraseña es requerida", Toast.LENGTH_SHORT).show();
            txtPassword.setBackgroundResource(R.drawable.error_border);
            return false;
        } else {
            txtPassword.setBackgroundResource(0);
        }

        return true;
    }

    private void iniciarSesion(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            // Extraer rol y cédula
                            String rol = jsonResponse.optString("rol");
                            String cedula = jsonResponse.optString("cedula");

                            Log.d("LOGIN_RESPONSE", "Rol: " + rol + ", Cédula: " + cedula);

                            // Guardar el rol y la cédula en SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_role", rol);
                            editor.putString("user_cedula", cedula);
                            editor.apply();

                            redirectToRoleActivity(rol);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("email", txtEmail.getText().toString());
                parametros.put("password", txtPassword.getText().toString());
                return parametros;
            }
        };

        requestQueue.add(stringRequest);
    }


    private void redirectToRoleActivity(String role) {
        Intent intent;
        switch (role) {
            case "cliente":
                intent = new Intent(getApplicationContext(), MenuCliente.class);
                break;
            case "soporte":
                intent = new Intent(getApplicationContext(), MenuSoporte.class);
                break;
            case "empleado":
                intent = new Intent(getApplicationContext(), MenuEmpleado.class);
                break;
            default:
                Toast.makeText(getApplicationContext(), "Rol no reconocido", Toast.LENGTH_SHORT).show();
                return;
        }
        startActivity(intent);
        finish();
    }
}
