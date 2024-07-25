package com.example.proyectoaula.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectoaula.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textWelcome = binding.textWelcome;
        final TextView textServices = binding.textServices;
        final TextView textReport = binding.textReport;
        final TextView textHistory = binding.textHistory;

        homeViewModel.getWelcomeText().observe(getViewLifecycleOwner(), textWelcome::setText);
        homeViewModel.getServicesText().observe(getViewLifecycleOwner(), textServices::setText);
        homeViewModel.getReportText().observe(getViewLifecycleOwner(), textReport::setText);
        homeViewModel.getHistoryText().observe(getViewLifecycleOwner(), textHistory::setText);

        // Agrega listeners para los elementos si es necesario
        binding.servicesCard.setOnClickListener(v -> {
            // Navegar a la pantalla de servicios
        });

        binding.reportCard.setOnClickListener(v -> {
            // Navegar a la pantalla de reporte de problemas
        });

        binding.historyCard.setOnClickListener(v -> {
            // Navegar a la pantalla de historial de pagos
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
