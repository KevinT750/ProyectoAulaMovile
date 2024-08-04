package com.example.proyectoaula;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.proyectoaula.databinding.ActivityMenuClienteBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MenuCliente extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuClienteBinding binding;
    private DrawerLayout drawer;
    private TextView txtUsuario;
    private TextView txtCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuClienteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenuCliente.toolbar);

        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_services,
                R.id.nav_report_issue, R.id.nav_report_history, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_cliente);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Obtener referencias a los TextViews
        View headerView = navigationView.getHeaderView(0);
        txtUsuario = headerView.findViewById(R.id.txtUsuario);
        txtCorreo = headerView.findViewById(R.id.txtCorreo);

        // Manejo de la acción de cerrar sesión
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_logout) {
                    logout();
                    return true;
                }
                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                if (handled) {
                    drawer.closeDrawer(navigationView);
                }
                return handled;
            }
        });

        // Obtener la cédula de SharedPreferences y enviar la solicitud
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String cedula = sharedPreferences.getString("user_cedula", null);
        if (cedula != null) {
            obtenerUsuario(cedula);
        } else {
            Toast.makeText(this, "Cédula no encontrada en preferencias", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cliente, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_cliente);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    private void logout() {
        // Limpiar preferencias compartidas
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirigir al usuario a la actividad de inicio de sesión
        Intent intent = new Intent(MenuCliente.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void obtenerUsuario(String cedula) {
        new ObtenerUsuarioTask().execute(cedula);
    }

    private class ObtenerUsuarioTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String cedula = params[0];
            String url = "http://169.254.132.108:8080/PoyectoAula/obtenerUser.php"; // Ajusta la IP según sea necesario
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String urlParameters = "cedula=" + URLEncoder.encode(cedula, "UTF-8");

                // Enviar solicitud POST
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                // Obtener la respuesta
                int responseCode = con.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(result);

                    String mensaje = jsonResponse.optString("mensaje");
                    if (!mensaje.isEmpty()) {
                        Toast.makeText(MenuCliente.this, mensaje, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String tipoUsuario = jsonResponse.optString("tipo_usuario");
                    String nombre = jsonResponse.optString("nombre");
                    String correo = jsonResponse.optString("correo");
                    String cedula = jsonResponse.optString("cedula");

                    // Guardar datos en SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("nombre", nombre);
                    if (tipoUsuario.equals("CLIENTE")) {
                        txtUsuario.setText(nombre);
                        txtCorreo.setText(correo);
                        editor.putString("correo", correo);
                    } else if (tipoUsuario.equals("SOPORTE") || tipoUsuario.equals("TECNICO")) {
                        txtUsuario.setText(nombre);
                        txtCorreo.setText(cedula); // Usar txtCorreo para mostrar la cédula
                        editor.putString("cedula", cedula);
                    }
                    editor.apply();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MenuCliente.this, "Error procesando la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MenuCliente.this, "Error al obtener datos del servidor", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
