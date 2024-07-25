package com.example.proyectoaula.ui.Reportar;

import androidx.lifecycle.ViewModelProvider;

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

import com.example.proyectoaula.R;
import com.example.proyectoaula.databinding.FragmentReportarBinding;

public class ReportarFragment extends Fragment {

    private FragmentReportarBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReportarViewModel reportarViewModel =
                new ViewModelProvider(this).get(ReportarViewModel.class);

        binding = FragmentReportarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textReportar;
        reportarViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Set up the Spinner
        Spinner spinnerHorario = binding.spinnerHorario;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.horarios_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorario.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
