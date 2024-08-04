package com.example.proyectoaula.ui.Reportar;

import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoaula.R;
import com.example.proyectoaula.databinding.FragmentReportarBinding;
import com.example.proyectoaula.Cliente;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReportarFragment extends Fragment {

    private FragmentReportarBinding binding;
    private ReportarViewModel reportarViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportarViewModel = new ViewModelProvider(this).get(ReportarViewModel.class);

        binding = FragmentReportarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textReportar;
        reportarViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Set up the Spinner for horarios
        Spinner spinnerHorario = binding.spinnerHorario;
        ArrayAdapter<CharSequence> adapterHorario = ArrayAdapter.createFromResource(getContext(),
                R.array.horarios_array, android.R.layout.simple_spinner_item);
        adapterHorario.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorario.setAdapter(adapterHorario);

        // Set up the Spinner for servicios
        Spinner spinnerServicio = binding.spinnerServicio;
        ArrayAdapter<String> adapterServicio = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        adapterServicio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServicio.setAdapter(adapterServicio);

        // Load servicios from server
        String cedula = Cliente.obtenerCedula(getContext());
        reportarViewModel.getServiciosActivos(cedula).observe(getViewLifecycleOwner(), servicios -> {
            adapterServicio.clear();
            adapterServicio.addAll(servicios);
            adapterServicio.notifyDataSetChanged();
        });

        // Set up the Spinner for problemas
        Spinner spinnerProblema = binding.spinnerProblema;
        ArrayAdapter<CharSequence> adapterProblema = ArrayAdapter.createFromResource(getContext(),
                R.array.problemas_array, android.R.layout.simple_spinner_item);
        adapterProblema.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProblema.setAdapter(adapterProblema);

        binding.btnEnviar.setOnClickListener(v -> {
            String selectedServicio = (String) spinnerServicio.getSelectedItem();
            Integer idServicio = reportarViewModel.getServicioId(selectedServicio);

            if (idServicio != null) {
                String tipoDano = (String) spinnerProblema.getSelectedItem();
                String horarioAtencion = (String) spinnerHorario.getSelectedItem();
                String detalle = binding.editTextDescripcion.getText().toString();

                enviarSolicitud(idServicio, tipoDano, horarioAtencion, detalle);
            } else {
                Toast.makeText(getContext(), "Por favor, seleccione un servicio válido", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void enviarSolicitud(int idServicio, String tipoDano, String horarioAtencion, String detalle) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    URL url = new URL("http://169.254.132.108:8080/PoyectoAula/InsertarSolicitud.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("idServicio", String.valueOf(idServicio))
                            .appendQueryParameter("idSoporte", "")  // Enviar vacío si no se utiliza
                            .appendQueryParameter("tipoDano", tipoDano)
                            .appendQueryParameter("horarioAtencion", horarioAtencion)
                            .appendQueryParameter("detalle", detalle);
                    String query = builder.build().getEncodedQuery();

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        JSONObject jsonResponse = new JSONObject(response.toString());
                        return jsonResponse.optBoolean("success", false);
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    Toast.makeText(getContext(), "Solicitud enviada con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error al enviar la solicitud", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
