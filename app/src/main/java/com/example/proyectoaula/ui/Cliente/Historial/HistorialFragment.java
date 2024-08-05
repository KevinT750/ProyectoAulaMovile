package com.example.proyectoaula.ui.Cliente.Historial;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoaula.HistorialAdapter;
import com.example.proyectoaula.R;
import com.example.proyectoaula.Orden;
import com.example.proyectoaula.Cliente;
import com.example.proyectoaula.databinding.FragmentHistorialBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HistorialFragment extends Fragment {

    private FragmentHistorialBinding binding;
    private HistorialViewModel historialViewModel;
    private RecyclerView recyclerView;
    private HistorialAdapter historialAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historialViewModel =
                new ViewModelProvider(this).get(HistorialViewModel.class);

        binding = FragmentHistorialBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerViewReportes;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        historialViewModel.getOrdenes().observe(getViewLifecycleOwner(), ordenes -> {
            historialAdapter = new HistorialAdapter(ordenes);
            recyclerView.setAdapter(historialAdapter);
        });

        obtenerOrdenes();

        return root;
    }

    private void obtenerOrdenes() {
        String cedula = Cliente.obtenerCedula(requireContext());
        if (cedula != null) {
            new ObtenerOrdenesTask().execute(cedula);
        }
    }

    private class ObtenerOrdenesTask extends AsyncTask<String, Void, List<Orden>> {

        @Override
        protected List<Orden> doInBackground(String... params) {
            String cedula = params[0];
            String url = "http://169.254.132.108:8080/PoyectoAula/ObtenerOrdenes.php?cedula=" + cedula;
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");

                int responseCode = con.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Convertir la respuesta a un JSONObject
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray jsonArray = jsonResponse.getJSONArray("data");

                List<Orden> ordenes = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Orden orden = new Orden();
                    orden.setIdOrden(jsonObject.optString("idOrden"));
                    orden.setFechaInicio(jsonObject.optString("fechaInicio"));
                    orden.setFechaFin(jsonObject.optString("fechaFin"));
                    orden.setDescripcion(jsonObject.optString("descripcion"));
                    orden.setEstado(jsonObject.optString("estado"));
                    ordenes.add(orden);
                }
                return ordenes;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Orden> result) {
            if (result != null) {
                historialViewModel.setOrdenes(result);
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
