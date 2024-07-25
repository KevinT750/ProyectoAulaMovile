package com.example.proyectoaula.ui.Reportar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReportarViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public ReportarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("REPORTAR PROBLEMAS");
    }

    public LiveData<String> getText() {
        return mText;
    }
}