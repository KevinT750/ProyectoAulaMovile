package com.example.proyectoaula.ui.Cliente.Servicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ServicioViewModel extends ViewModel {

    private final MutableLiveData<String> nombre = new MutableLiveData<>();
    private final MutableLiveData<String> ciudad = new MutableLiveData<>();
    private final MutableLiveData<String> callePrincipal = new MutableLiveData<>();
    private final MutableLiveData<String> calleSecundaria = new MutableLiveData<>();
    private final MutableLiveData<String> referencia = new MutableLiveData<>();
    private final MutableLiveData<String> tipoServicio = new MutableLiveData<>();

    public LiveData<String> getNombre() {
        return nombre;
    }

    public LiveData<String> getCiudad() {
        return ciudad;
    }

    public LiveData<String> getCallePrincipal() {
        return callePrincipal;
    }

    public LiveData<String> getCalleSecundaria() {
        return calleSecundaria;
    }

    public LiveData<String> getReferencia() {
        return referencia;
    }

    public LiveData<String> getTipoServicio() {
        return tipoServicio;
    }

    public void setNombre(String nombre) {
        this.nombre.setValue(nombre);
    }

    public void setCiudad(String ciudad) {
        this.ciudad.setValue(ciudad);
    }

    public void setCallePrincipal(String callePrincipal) {
        this.callePrincipal.setValue(callePrincipal);
    }

    public void setCalleSecundaria(String calleSecundaria) {
        this.calleSecundaria.setValue(calleSecundaria);
    }

    public void setReferencia(String referencia) {
        this.referencia.setValue(referencia);
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio.setValue(tipoServicio);
    }
}
