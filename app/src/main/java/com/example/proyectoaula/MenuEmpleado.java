package com.example.proyectoaula;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

import com.example.proyectoaula.databinding.ActivityMenuEmpleadoBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MenuEmpleado extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuEmpleadoBinding binding;
    private DrawerLayout drawer;
    private TextView txtUsuario;
    private TextView txtCedula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuEmpleadoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenuEmpleado.toolbar);

        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_empleado);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Obtener referencias a los TextViews
        View headerView = navigationView.getHeaderView(0);
        txtUsuario = headerView.findViewById(R.id.txtUsuario);
        txtCedula = headerView.findViewById(R.id.txtCedula);

        // Manejo de la acción de cerrar sesión
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_slideshow) {
                    cerrarSesion();
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
        getMenuInflater().inflate(R.menu.menu_empleado, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_empleado);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    private void cerrarSesion() {
        // Limpiar SharedPreferences para cerrar sesión
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Limpiar todos los datos guardados
        editor.apply();

        // Redirigir al usuario al MainActivity
        Intent intent = new Intent(MenuEmpleado.this, MainActivity.class);
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
                        Toast.makeText(MenuEmpleado.this, mensaje, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String nombre = jsonResponse.optString("nombre");
                    String cedula = jsonResponse.optString("cedula");

                    // Guardar datos en SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("nombre", nombre);
                    editor.putString("cedula", cedula);
                    editor.apply();

                    // Mostrar los datos en el menú
                    txtUsuario.setText(nombre);
                    txtCedula.setText(cedula);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MenuEmpleado.this, "Error procesando la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MenuEmpleado.this, "Error al obtener datos del servidor", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
