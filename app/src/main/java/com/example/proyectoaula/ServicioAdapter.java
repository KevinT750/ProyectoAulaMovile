package com.example.proyectoaula;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ServicioAdapter extends RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder> {

    private List<Servicio> servicioList;

    public ServicioAdapter(List<Servicio> servicios) {
        this.servicioList = servicios;
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servicio, parent, false);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {
        Servicio servicio = servicioList.get(position);
        holder.bind(servicio, position + 1);
    }

    @Override
    public int getItemCount() {
        return servicioList.size();
    }

    static class ServicioViewHolder extends RecyclerView.ViewHolder {

        private TextView textServicioNumero;
        private TextView textNombre;
        private TextView textDetalles;
        private Button buttonGuardarId; // Botón para guardar la ID del servicio

        public ServicioViewHolder(@NonNull View itemView) {
            super(itemView);
            textServicioNumero = itemView.findViewById(R.id.textServicioNumero);
            textNombre = itemView.findViewById(R.id.textNombreApellido);
            textDetalles = itemView.findViewById(R.id.textDetalles);
            buttonGuardarId = itemView.findViewById(R.id.buttonGuardarId); // Inicializar el botón
        }

        public void bind(Servicio servicio, int numero) {
            textServicioNumero.setText("Servicio: " + numero);
            textNombre.setText(servicio.getNombre());
            textDetalles.setText(Html.fromHtml(servicio.getDetalleServicio()));

            // Configurar el botón para guardar la ID del servicio
            buttonGuardarId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guardarIdServicio(itemView.getContext(), servicio.getIdServicio());
                }
            });
        }

        // Método para guardar la ID del servicio en SharedPreferences
        private void guardarIdServicio(Context context, String idServicio) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("servicio_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("id_servicio", idServicio);
            editor.apply();
        }
    }
}
