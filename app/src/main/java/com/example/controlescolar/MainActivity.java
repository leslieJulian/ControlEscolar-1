package com.example.controlescolar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnRegistrar, btnVisualizar, btnMaterias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(registrar);
        btnVisualizar = findViewById(R.id.btnVisualizar);
        btnVisualizar.setOnClickListener(visualizar);
        btnMaterias = findViewById(R.id.btnCargarMaterias);
        btnMaterias.setOnClickListener(irMaterias);
    }

    //EVENTOS PARA ABRIR OTRA ACTIVITY
    View.OnClickListener registrar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), RegistrarEstudiante.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };
    View.OnClickListener visualizar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), VisualizarEstudiante.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };
    View.OnClickListener irMaterias = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), ActivityMaterias.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };

}
