package com.example.proyectoaula;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class Registrarse extends AppCompatActivity {
    private ImageButton btnRegresar;
    private Button btnRegistrar;
    private EditText txtCedula, txtNombre, txtApellido, txtTelefono, txtCorreo, txtContrasenia, txtConfirmarContrasenia;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrarse);

        btnRegresar = findViewById(R.id.btnRegresarI);
        btnRegistrar = findViewById(R.id.btnGuardar);
        txtCedula = findViewById(R.id.txtCedula);
        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContrasenia = findViewById(R.id.txtPassW);
        txtConfirmarContrasenia = findViewById(R.id.txtConfir);

        requestQueue = Volley.newRequestQueue(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnRegresar.setOnClickListener(view -> finish());

        // Añadir TextWatchers para validaciones en tiempo real
        txtCedula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validaCedula(s.toString())) {
                    txtCedula.setBackgroundColor(Color.GREEN);
                } else {
                    txtCedula.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        txtNombre.addTextChangedListener(new CapitalizationTextWatcher(txtNombre));
        txtApellido.addTextChangedListener(new CapitalizationTextWatcher(txtApellido));

        txtTelefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches("^09\\d{8}$")) {
                    txtTelefono.setBackgroundColor(Color.GREEN);
                } else {
                    txtTelefono.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        txtCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches("^[\\w.-]+@(gmail\\.com|hotmail\\.com|yahoo\\.com)$")) {
                    txtCorreo.setBackgroundColor(Color.GREEN);
                } else {
                    txtCorreo.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        txtContrasenia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$")) {
                    txtContrasenia.setBackgroundColor(Color.GREEN);
                } else {
                    txtContrasenia.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        txtConfirmarContrasenia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(txtContrasenia.getText().toString())) {
                    txtConfirmarContrasenia.setBackgroundColor(Color.GREEN);
                } else {
                    txtConfirmarContrasenia.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnRegistrar.setOnClickListener(view -> {
            if (validateInput()) {
                registrarUsuario("http://169.254.132.108:8080/PoyectoAula/IngresarCliente.php");
            }
        });
    }

    private boolean validateInput() {
        String cedula = txtCedula.getText().toString();
        String nombre = txtNombre.getText().toString();
        String apellido = txtApellido.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String correo = txtCorreo.getText().toString();
        String contrasenia = txtContrasenia.getText().toString();
        String confirmarContrasenia = txtConfirmarContrasenia.getText().toString();

        if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || correo.isEmpty() ||
                contrasenia.isEmpty() || confirmarContrasenia.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!contrasenia.equals(confirmarContrasenia)) {
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void registrarUsuario(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("cedula", txtCedula.getText().toString());
                parametros.put("nombre", txtNombre.getText().toString());
                parametros.put("apellido", txtApellido.getText().toString());
                parametros.put("telefono", txtTelefono.getText().toString());
                parametros.put("correo", txtCorreo.getText().toString());
                parametros.put("contrasenia", txtContrasenia.getText().toString());
                return parametros;
            }
        };

        requestQueue.add(stringRequest);
    }

    private boolean validaCedula(String x) {
        int suma = 0;
        if (x.length() != 10 || x.matches("0{10}")) {
            return false;
        } else {
            int a[] = new int[x.length() / 2];
            int b[] = new int[(x.length() / 2)];
            int c = 0;
            int d = 1;
            for (int i = 0; i < x.length() / 2; i++) {
                a[i] = Integer.parseInt(String.valueOf(x.charAt(c)));
                c = c + 2;
                if (i < (x.length() / 2) - 1) {
                    b[i] = Integer.parseInt(String.valueOf(x.charAt(d)));
                    d = d + 2;
                }
            }

            for (int i = 0; i < a.length; i++) {
                a[i] = a[i] * 2;
                if (a[i] > 9) {
                    a[i] = a[i] - 9;
                }
                suma = suma + a[i] + b[i];
            }
            int aux = suma / 10;
            int dec = (aux + 1) * 10;
            if ((dec - suma) == Integer.parseInt(String.valueOf(x.charAt(x.length() - 1))))
                return true;
            else if (suma % 10 == 0 && x.charAt(x.length() - 1) == '0') {
                return true;
            } else {
                return false;
            }
        }
    }

    private class CapitalizationTextWatcher implements TextWatcher {
        private EditText editText;

        public CapitalizationTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().isEmpty() && !Character.isUpperCase(s.charAt(0))) {
                editText.setBackgroundColor(Color.RED);
            } else {
                editText.setBackgroundColor(Color.GREEN);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    }
}
