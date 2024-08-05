package com.example.proyectoaula.ui.Tecnico.Reporte;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectoaula.R;
import com.example.proyectoaula.Solicitud;
import com.example.proyectoaula.SolicitudAdapter;
import java.util.List;

public class ReportesTFragment extends Fragment {

    private ReportesTViewModel mViewModel;
    private RecyclerView recyclerView;
    private SolicitudAdapter solicitudAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reportes_t, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_solicitud);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Pasar contexto al adaptador
        solicitudAdapter = new SolicitudAdapter(requireContext());
        recyclerView.setAdapter(solicitudAdapter);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReportesTViewModel.class);
        mViewModel.getSolicitudes().observe(getViewLifecycleOwner(), new Observer<List<Solicitud>>() {
            @Override
            public void onChanged(List<Solicitud> solicitudes) {
                solicitudAdapter.setSolicitudes(solicitudes);
            }
        });
    }
}