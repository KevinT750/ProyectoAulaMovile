package com.example.proyectoaula.ui.Servicio;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyectoaula.databinding.FragmentPerfilBinding;
import com.example.proyectoaula.databinding.FragmentServicioBinding;
import com.example.proyectoaula.ui.Perfil.PerfilViewModel;

public class ServicioFragment extends Fragment {

    private FragmentServicioBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ServicioViewModel servicioViewModel =
                new ViewModelProvider(this).get(ServicioViewModel.class);

        binding = FragmentServicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}