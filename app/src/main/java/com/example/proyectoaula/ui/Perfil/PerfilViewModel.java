package com.example.proyectoaula.ui.Perfil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PerfilViewModel extends ViewModel {

    private final MutableLiveData<String> nombre = new MutableLiveData<>();
    private final MutableLiveData<String> telefono = new MutableLiveData<>();
    private final MutableLiveData<String> correo = new MutableLiveData<>();
    private final MutableLiveData<String> ocupacion = new MutableLiveData<>();
    private final MutableLiveData<String> contrasena = new MutableLiveData<>();

    public LiveData<String> getNombre() {
        return nombre;
    }

    public LiveData<String> getTelefono() {
        return telefono;
    }

    public LiveData<String> getCorreo() {
        return correo;
    }

    public LiveData<String> getOcupacion() {
        return ocupacion;
    }

    public LiveData<String> getContrasena() {
        return contrasena;
    }

    public void setNombre(String nombre) {
        this.nombre.setValue(nombre);
    }

    public void setTelefono(String telefono) {
        this.telefono.setValue(telefono);
    }

    public void setCorreo(String correo) {
        this.correo.setValue(correo);
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion.setValue(ocupacion);
    }

    public void setContrasena(String contrasena) {
        this.contrasena.setValue(contrasena);
    }
}
