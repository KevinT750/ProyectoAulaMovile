package com.example.proyectoaula.ui.Servicio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoaula.R;
import com.example.proyectoaula.Servicio;
import com.example.proyectoaula.ServicioAdapter;
import com.example.proyectoaula.databinding.FragmentServicioBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ServicioFragment extends Fragment {

    private FragmentServicioBinding binding;
    private ServicioViewModel servicioViewModel;
    private RecyclerView recyclerView;
    private ServicioAdapter adapter;
    private List<Servicio> servicioList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        servicioViewModel = new ViewModelProvider(this).get(ServicioViewModel.class);

        binding = FragmentServicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerViewServicios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ServicioAdapter(servicioList);
        recyclerView.setAdapter(adapter);

        obtenerServiciosCliente();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                DividerItemDecoration.VERTICAL
        );
        recyclerView.addItemDecoration(dividerItemDecoration);

        return root;
    }

    private void obtenerServiciosCliente() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String cedula = sharedPreferences.getString("user_cedula", null);
        if (cedula != null) {
            new ObtenerServiciosTask().execute(cedula);
        } else {
            Toast.makeText(getActivity(), "Cédula no encontrada en preferencias", Toast.LENGTH_SHORT).show();
        }
    }

    private class ObtenerServiciosTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String cedula = params[0];
            String url = "http://169.254.132.108:8080/ProyectoAulaWeb/proyecto-agenda/controlador/ObtenerServicios.php";
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
                    JSONArray dataArray = jsonResponse.optJSONArray("data");

                    if (dataArray != null && dataArray.length() > 0) {
                        servicioList.clear();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject data = dataArray.getJSONObject(i);

                            // Obtener los datos
                            String idServicio = data.optString("IDSERVICIO");
                            String nombre = data.optString("IDCLIENTE");
                            String ciudad = data.optString("CIUDAD");
                            String callePrincipal = data.optString("CALLEPRINCIPAL");
                            String calleSecundaria = data.optString("CALLESECUNDARIA");
                            String referencia = data.optString("REFERENCIA");
                            String tipoServicio = data.optString("TIPOSERVICIO");

                            // Crear el objeto Servicio y establecer los datos
                            Servicio servicio = new Servicio();
                            servicio.setIdServicio(idServicio);
                            servicio.setNombre(nombre);
                            servicio.setCiudad(ciudad);
                            servicio.setCallePrincipal(callePrincipal);
                            servicio.setCalleSecundaria(calleSecundaria);
                            servicio.setReferencia(referencia);
                            servicio.setTipoServicio(tipoServicio);

                            // Añadir a la lista
                            servicioList.add(servicio);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "No se encontraron servicios para esta cédula", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error procesando la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Error al obtener datos del servidor", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
