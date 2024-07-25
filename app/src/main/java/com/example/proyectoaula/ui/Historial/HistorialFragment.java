package com.example.proyectoaula.ui.Historial;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyectoaula.R;
import com.example.proyectoaula.databinding.FragmentHistorialBinding;
import com.example.proyectoaula.databinding.FragmentReportarBinding;
import com.example.proyectoaula.ui.Reportar.ReportarViewModel;

public class HistorialFragment extends Fragment {

    private FragmentHistorialBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HistorialViewModel historialViewModel =
                new ViewModelProvider(this).get(HistorialViewModel.class);

        binding = FragmentHistorialBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}