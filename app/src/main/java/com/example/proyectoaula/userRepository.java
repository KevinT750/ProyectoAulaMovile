package com.example.proyectoaula;

import android.content.Context;
import android.content.SharedPreferences;

public class userRepository {

    private SharedPreferences sharedPreferences;

    public userRepository(Context context) {
        sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
    }

    public String getUsername() {
        return sharedPreferences.getString("username", "Nombre no disponible");
    }

    public String getEmail() {
        return sharedPreferences.getString("email", "Correo no disponible");
    }

    // Puedes agregar métodos para actualizar los datos si es necesario
    public void saveUserData(String username, String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("email", email);
        editor.apply();
    }
}
