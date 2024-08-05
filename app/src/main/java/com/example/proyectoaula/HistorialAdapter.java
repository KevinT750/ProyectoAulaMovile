package com.example.proyectoaula;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.ViewHolder> {

    private final List<Orden> ordenes;

    public HistorialAdapter(List<Orden> ordenes) {
        this.ordenes = ordenes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_orden, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Orden orden = ordenes.get(position);

        // Usando el método getConcatenatedInfo() para mostrar toda la información en un solo TextView
        holder.txtIdOrden.setText(orden.getOrdenInfo());
        holder.txtConcatenatedInfo.setText(orden.getConcatenatedInfo());
    }

    @Override
    public int getItemCount() {
        return ordenes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtIdOrden;
        public TextView txtConcatenatedInfo; // TextView para la información concatenada

        public ViewHolder(View view) {
            super(view);
            txtIdOrden = view.findViewById(R.id.txtIdOrden);
            txtConcatenatedInfo = view.findViewById(R.id.txtConcatenatedInfo); // Inicializar el TextView para la información concatenada
        }
    }
}
