package com.example.proyectoaula.ui.Perfil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectoaula.R;
import com.example.proyectoaula.databinding.FragmentPerfilBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class PerfilFragment extends Fragment {

    private EditText txtNombre;
    private EditText txtTelefono;
    private EditText txtCorreo;
    private EditText txtOcupacion;
    private EditText txtPassword;

    private ImageButton btnNombre;
    private ImageButton btnTelefono;
    private ImageButton btnCorreo;
    private ImageButton btnContrasena;

    private FragmentPerfilBinding binding;
    private PerfilViewModel perfilViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        txtNombre = root.findViewById(R.id.txtNombreC);
        txtTelefono = root.findViewById(R.id.txtTelefonoC);
        txtCorreo = root.findViewById(R.id.txtEmailC);
        txtOcupacion = root.findViewById(R.id.txtOcupacion);
        txtPassword = root.findViewById(R.id.txtPassword);

        btnNombre = root.findViewById(R.id.btnNombre);
        btnTelefono = root.findViewById(R.id.btnTelefono);
        btnCorreo = root.findViewById(R.id.btnEmail);
        btnContrasena = root.findViewById(R.id.btnPassword);

        txtNombre.setEnabled(false);
        txtTelefono.setEnabled(false);
        txtCorreo.setEnabled(false);
        txtOcupacion.setEnabled(false);
        txtPassword.setEnabled(false);

        btnNombre.setOnClickListener(v -> txtNombre.setEnabled(!txtNombre.isEnabled()));
        btnTelefono.setOnClickListener(v -> txtTelefono.setEnabled(!txtTelefono.isEnabled()));
        btnCorreo.setOnClickListener(v -> txtCorreo.setEnabled(!txtCorreo.isEnabled()));
        btnContrasena.setOnClickListener(v -> newPassword());

        perfilViewModel.getNombre().observe(getViewLifecycleOwner(), txtNombre::setText);
        perfilViewModel.getTelefono().observe(getViewLifecycleOwner(), txtTelefono::setText);
        perfilViewModel.getCorreo().observe(getViewLifecycleOwner(), txtCorreo::setText);
        perfilViewModel.getOcupacion().observe(getViewLifecycleOwner(), txtOcupacion::setText);
        perfilViewModel.getContrasena().observe(getViewLifecycleOwner(), txtPassword::setText);

        obtenerUsuario();

        return root;
    }

    private void obtenerUsuario() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String cedula = sharedPreferences.getString("user_cedula", null);
        if (cedula != null) {
            new ObtenerUsuarioTask().execute(cedula);
        } else {
            Toast.makeText(getActivity(), "Cédula no encontrada en preferencias", Toast.LENGTH_SHORT).show();
        }
    }

    private class ObtenerUsuarioTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String cedula = params[0];
            String url = "http://169.254.132.108:8080/PoyectoAula/Users.php";
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String urlParameters = "cedula=" + URLEncoder.encode(cedula, "UTF-8");

                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

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
                        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String nombre = jsonResponse.optString("nombre");
                    String telefono = jsonResponse.optString("nrotelefono");
                    String correo = jsonResponse.optString("correo");
                    String ocupacion = jsonResponse.optString("rol");
                    String contrasena = jsonResponse.optString("contrasenia");

                    perfilViewModel.setNombre(nombre);
                    perfilViewModel.setTelefono(telefono);
                    perfilViewModel.setCorreo(correo);
                    perfilViewModel.setOcupacion(ocupacion);
                    perfilViewModel.setContrasena(contrasena);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error procesando la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Error al obtener datos del servidor", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void newPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.popup_modify_password, null);
        builder.setView(dialogView);

        EditText txtPasswordAc = dialogView.findViewById(R.id.txtPasswordAc);
        EditText txtNewPassword = dialogView.findViewById(R.id.txtNewPassword);
        EditText txtConfirmPassword = dialogView.findViewById(R.id.txtConfirmPassword);
        Button btnSavePassword = dialogView.findViewById(R.id.btnSavePassword);

        AlertDialog dialog = builder.create();

        btnSavePassword.setOnClickListener(v -> {
            String passwordAc = txtPasswordAc.getText().toString().trim();
            String newPassword = txtNewPassword.getText().toString().trim();
            String confirmPassword = txtConfirmPassword.getText().toString().trim();

            if (newPassword.equals(confirmPassword)) {
                obtenerCedulaYActualizarContrasena(passwordAc, newPassword);
            } else {
                Toast.makeText(getActivity(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    private void obtenerCedulaYActualizarContrasena(String passwordAc, String newPassword) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String cedula = sharedPreferences.getString("user_cedula", null);
        if (cedula != null) {
            new ActualizarContrasenaTask().execute(cedula, passwordAc, newPassword);
        } else {
            Toast.makeText(getActivity(), "Cédula no encontrada en preferencias", Toast.LENGTH_SHORT).show();
        }
    }

    private class ActualizarContrasenaTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String cedula = params[0];
            String passwordAc = params[1];
            String newPassword = params[2];
            String url = "http://169.254.132.108:8080/PoyectoAula/CambiarContrasenia.php";

            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String urlParameters = "cedula=" + URLEncoder.encode(cedula, "UTF-8") +
                        "&password_ac=" + URLEncoder.encode(passwordAc, "UTF-8") +
                        "&new_password=" + URLEncoder.encode(newPassword, "UTF-8");

                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

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
                Toast.makeText(getActivity(), "Respuesta del servidor: " + result, Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    String mensaje = jsonResponse.optString("mensaje");
                    Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error procesando la respuesta del servidor: " + result, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
            }
        }
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
