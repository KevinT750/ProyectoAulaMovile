package com.example.proyectoaula.ui.Cliente.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> welcomeText;
    private final MutableLiveData<String> servicesText;
    private final MutableLiveData<String> reportText;
    private final MutableLiveData<String> historyText;

    public HomeViewModel() {
        welcomeText = new MutableLiveData<>();
        servicesText = new MutableLiveData<>();
        reportText = new MutableLiveData<>();
        historyText = new MutableLiveData<>();

        welcomeText.setValue("¡Bienvenido a tu cuenta!");
        servicesText.setValue("Servicios de Internet");
        reportText.setValue("Informar problemas");
        historyText.setValue("H |istorial de pagos");
    }

    public LiveData<String> getWelcomeText() {
        return welcomeText;
    }

    public LiveData<String> getServicesText() {
        return servicesText;
    }

    public LiveData<String> getReportText() {
        return reportText;
    }

    public LiveData<String> getHistoryText() {
        return historyText;
    }
}
