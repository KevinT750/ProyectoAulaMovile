package com.example.proyectoaula;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import org.json.JSONException;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button btnRegistrarse, btnIniciarSesion, btnOlvide;
    private EditText txtEmail, txtPassword;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String userRole = sharedPreferences.getString("user_role", null);
        if (userRole != null) {
            redirectToRoleActivity(userRole);
        }

        btnRegistrarse = findViewById(R.id.btnReg);
        btnIniciarSesion = findViewById(R.id.btnIni);
        btnOlvide = findViewById(R.id.btnPass);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPass);

        requestQueue = Volley.newRequestQueue(this);

        btnRegistrarse.setOnClickListener(view -> {
            Intent activar = new Intent(MainActivity.this, Registrarse.class);
            startActivity(activar);
        });

        btnIniciarSesion.setOnClickListener(view -> {
            if (validateInput()) {
                iniciarSesion("http://169.254.132.108:8080/PoyectoAula/Login.php");
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

                            if (jsonResponse.has("error")) {
                                Toast.makeText(getApplicationContext(), jsonResponse.getString("error"), Toast.LENGTH_SHORT).show();
                            } else {
                                String rol = jsonResponse.optString("rol");
                                String cedula = jsonResponse.optString("cedula");

                                Log.d("LOGIN_RESPONSE", "Rol: " + rol + ", Cédula: " + cedula);

                                SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("user_role", rol);
                                editor.putString("user_cedula", cedula);
                                editor.apply();

                                redirectToRoleActivity(rol);
                            }
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
            case "CLIENTE":
                intent = new Intent(getApplicationContext(), MenuCliente.class);
                break;
            case "SOPORTE":
                intent = new Intent(getApplicationContext(), MenuSoporte.class);
                break;
            case "TECNICO":
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
