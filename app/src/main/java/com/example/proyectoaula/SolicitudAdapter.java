package com.example.proyectoaula;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoaula.R;
import com.example.proyectoaula.Solicitud;
import com.example.proyectoaula.Cliente;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SolicitudAdapter extends RecyclerView.Adapter<SolicitudAdapter.ViewHolder> {

    private List<Solicitud> solicitudList = new ArrayList<>();
    private String idSoporteSeleccionado;
    private String idSolicitudSeleccionado; // Variable para almacenar el ID de la solicitud
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewSolicitudInfo;
        public TextView textViewConcatenatedInfo;
        public Button buttonAccept;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewSolicitudInfo = itemView.findViewById(R.id.text_view_id_solicitud);
            textViewConcatenatedInfo = itemView.findViewById(R.id.text_view_concatenated_info);
            buttonAccept = itemView.findViewById(R.id.button_accept);
        }
    }

    public SolicitudAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_solicitud, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Solicitud solicitud = solicitudList.get(position);
        holder.textViewSolicitudInfo.setText(solicitud.getSolicitudInfo());
        holder.textViewConcatenatedInfo.setText(solicitud.getConcatenatedInfo());
        holder.buttonAccept.setTag(solicitud.getIdSoporte());

        holder.buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idSoporteSeleccionado = (String) v.getTag();
                idSolicitudSeleccionado = String.valueOf(solicitud.getIdSolicitud()); // Guardar el ID de la solicitud
                Log.d("SolicitudAdapter", "ID de soporte seleccionado: " + idSoporteSeleccionado);
                showOrderDialog(); // Mostrar el diálogo cuando se haga clic
            }
        });
    }

    @Override
    public int getItemCount() {
        return solicitudList.size();
    }

    public void setSolicitudes(List<Solicitud> solicitudes) {
        this.solicitudList = solicitudes;
        notifyDataSetChanged();
    }

    public String getIdSoporteSeleccionado() {
        return idSoporteSeleccionado;
    }

    private void showOrderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.activity_create_order, null);
        builder.setView(dialogView);

        EditText txtRelevancia = dialogView.findViewById(R.id.txtRelevancia);
        EditText txtEstado = dialogView.findViewById(R.id.txtEstado);
        EditText txtObservacionTecnico = dialogView.findViewById(R.id.txtObservacionTecnico);
        EditText txtFechaElaboracion = dialogView.findViewById(R.id.txtFechaElaboracion);
        Button btnSaveOrder = dialogView.findViewById(R.id.btnSaveOrder);

        // Configurar el DatePickerDialog para txtFechaElaboracion
        txtFechaElaboracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtFechaElaboracion);
            }
        });

        AlertDialog dialog = builder.create();

        // Configurar la acción para el botón de guardar
        btnSaveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener cédula del técnico desde SharedPreferences
                String idTecnico = Cliente.obtenerCedula(context);
                if (idTecnico != null) {
                    String relevancia = txtRelevancia.getText().toString().trim();
                    String fechaElaboracion = txtFechaElaboracion.getText().toString().trim();
                    String estado = txtEstado.getText().toString().trim();
                    String observacionTecnico = txtObservacionTecnico.getText().toString().trim();

                    // Llamar al método para guardar la orden
                    saveOrder(idSolicitudSeleccionado, idTecnico, relevancia, fechaElaboracion, estado, observacionTecnico);
                } else {
                    Toast.makeText(context, "Cédula del técnico no encontrada", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss(); // Cerrar el diálogo
            }
        });

        dialog.show();
    }

    private void showDatePickerDialog(final EditText dateEditText) {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear un DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Formatear la fecha como YYYY-MM-DD
                        String formattedDate = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                        dateEditText.setText(formattedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void saveOrder(String idSolicitud, String idTecnico, String relevancia, String fechaElaboracion, String estado, String observacionTecnico) {
        Log.d("SolicitudAdapter", "Guardando orden con ID de solicitud: " + idSolicitud);
        new GuardarOrdenTask().execute(idSolicitud, idTecnico, relevancia, fechaElaboracion, estado, observacionTecnico);
    }

    private class GuardarOrdenTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String idSolicitud = params[0];
            String idTecnico = params[1];
            String relevancia = params[2];
            String fechaElaboracion = params[3];
            String estado = params[4];
            String observacionTecnico = params[5];
            String url = "http://169.254.132.108:8080/PoyectoAula/insertarOrden.php"; // Reemplaza con la URL real

            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String urlParameters = "idSolicitud=" + URLEncoder.encode(idSolicitud, "UTF-8") +
                        "&idTecnico=" + URLEncoder.encode(idTecnico, "UTF-8") +
                        "&relevancia=" + URLEncoder.encode(relevancia, "UTF-8") +
                        "&fechaElaboracion=" + URLEncoder.encode(fechaElaboracion, "UTF-8") +
                        "&estado=" + URLEncoder.encode(estado, "UTF-8") +
                        "&observacionTecnico=" + URLEncoder.encode(observacionTecnico, "UTF-8");

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
                    boolean success = jsonResponse.optBoolean("success", false);
                    String mensaje = jsonResponse.optString("message", "Error desconocido");

                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error procesando la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Error al guardar la orden", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
