package com.example.proyectoaula.ui.Servicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ServicioViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public ServicioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Servicio fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}