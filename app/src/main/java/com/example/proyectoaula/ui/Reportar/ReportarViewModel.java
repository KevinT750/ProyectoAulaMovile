package com.example.proyectoaula.ui.Reportar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportarViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<String>> mServicios;
    private Map<String, Integer> serviciosMap;

    public ReportarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("REPORTAR PROBLEMAS");

        mServicios = new MutableLiveData<>();
        serviciosMap = new HashMap<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<String>> getServiciosActivos(String cedula) {
        new ObtenerServiciosTask().execute(cedula);
        return mServicios;
    }

    public Integer getServicioId(String servicio) {
        return serviciosMap.get(servicio);
    }

    private class ObtenerServiciosTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... params) {
            String cedula = params[0];
            List<String> servicios = new ArrayList<>();

            try {
                URL url = new URL("http://169.254.132.108:8080/PoyectoAula/ObtenerServ.php?cedula=" + cedula);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONArray jsonArray = new JSONArray(result.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int idServicio = jsonObject.getInt("IDSERVICIO");
                    String ciudadYTipoServicio = jsonObject.getString("CiudadYTipoServicio");
                    String formattedService = "Servicio " + (i + 1) + ": " + ciudadYTipoServicio;
                    servicios.add(formattedService);
                    serviciosMap.put(formattedService, idServicio);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return servicios;
        }

        @Override
        protected void onPostExecute(List<String> servicios) {
            mServicios.setValue(servicios);
        }
    }
}
