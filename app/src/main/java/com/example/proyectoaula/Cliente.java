package com.example.proyectoaula;

import android.content.Context;
import android.content.SharedPreferences;

public class Cliente {
    private String Cedula;
    private String Nombre;
    private String Apellido;
    private String Telefono;
    private String Email;
    private String Contrasenia;

    // Constructor vacío
    public Cliente() {
    }

    // Constructor con parámetros
    public Cliente(String Cedula, String Nombre, String Apellido, String Telefono, String Email, String Contrasenia) {
        this.Cedula = Cedula;
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Telefono = Telefono;
        this.Email = Email;
        this.Contrasenia = Contrasenia;
    }

    // Getters y Setters
    public String getCedula() {
        return Cedula;
    }

    public void setCedula(String Cedula) {
        this.Cedula = Cedula;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getContrasenia() {
        return Contrasenia;
    }

    public void setContrasenia(String Contrasenia) {
        this.Contrasenia = Contrasenia;
    }

    // Método para obtener la cédula del cliente desde SharedPreferences
    public static String obtenerCedula(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_cedula", null);
    }
}
