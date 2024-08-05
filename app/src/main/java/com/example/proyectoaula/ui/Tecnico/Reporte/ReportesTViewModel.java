package com.example.proyectoaula.ui.Tecnico.Reporte;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.os.AsyncTask;
import com.example.proyectoaula.Solicitud;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReportesTViewModel extends ViewModel {
    private final MutableLiveData<List<Solicitud>> solicitudesLiveData;

    public ReportesTViewModel() {
        solicitudesLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Solicitud>> getSolicitudes() {
        new ObtenerSolicitudesTask().execute();
        return solicitudesLiveData;
    }

    private class ObtenerSolicitudesTask extends AsyncTask<Void, Void, List<Solicitud>> {
        @Override
        protected List<Solicitud> doInBackground(Void... voids) {
            List<Solicitud> solicitudes = new ArrayList<>();
            try {
                URL url = new URL("http://169.254.132.108:8080/PoyectoAula/obtenerSolicitus.php");
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
                    int idSolicitud = jsonObject.getInt("IDSOLICITUD");
                    int idServicio = jsonObject.getInt("IDSERVICIO");
                    String idSoporte = jsonObject.getString("IDSOPORTE");
                    String tipoDano = jsonObject.getString("TIPODANO");
                    String horarioAtencion = jsonObject.getString("HORARIOATENCION");
                    String fechaEmision = jsonObject.getString("FECHAEMISION");
                    int estado = jsonObject.getInt("ESTADO");
                    String detalle = jsonObject.getString("DETALLE");
                    String idCliente = jsonObject.getString("IDCLIENTE");
                    String ciudad = jsonObject.getString("CIUDAD");
                    String callePrincipal = jsonObject.getString("CALLEPRINCIPAL");
                    String calleSecundaria = jsonObject.getString("CALLESECUNDARIA");
                    String referencia = jsonObject.getString("REFERENCIA");
                    String tipoServicio = jsonObject.getString("TIPOSERVICIO");
                    String servicioEstado = jsonObject.getString("SERVICIO_ESTADO");
                    String clienteNombre = jsonObject.getString("CLIENTE_NOMBRE");

                    Solicitud solicitud = new Solicitud(idSolicitud, idServicio, idSoporte, tipoDano, horarioAtencion, fechaEmision, estado, detalle, idCliente, ciudad, callePrincipal, calleSecundaria, referencia, tipoServicio, servicioEstado, clienteNombre);
                    solicitudes.add(solicitud);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return solicitudes;
        }

        @Override
        protected void onPostExecute(List<Solicitud> solicitudes) {
            solicitudesLiveData.setValue(solicitudes);
        }
    }
}
