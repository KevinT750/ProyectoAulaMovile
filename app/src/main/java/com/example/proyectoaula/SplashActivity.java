package com.example.proyectoaula;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Cargar el GIF usando Glide
        ImageView gifImageView = findViewById(R.id.imageView6);
        Glide.with(this).asGif().load(R.drawable.icons8_carga_unscreen).into(gifImageView);

        // Agregar un retraso antes de iniciar la siguiente actividad
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Iniciar la siguiente actividad (MainActivity para login)
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000); // 3000 milisegundos = 3 segundos
    }
}
