package com.example.proyectoaula.ui.Cliente.Historial;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectoaula.Orden;

import java.util.List;

public class HistorialViewModel extends ViewModel {
    private final MutableLiveData<List<Orden>> ordenes;

    public HistorialViewModel() {
        ordenes = new MutableLiveData<>();
    }

    public LiveData<List<Orden>> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<Orden> ordenes) {
        this.ordenes.setValue(ordenes);
    }
}
