package com.example.controlescolar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Prueba para cambiar el texto por el nombre del instituto

        FirebaseDatabase.getInstance().getReference().child("datos").child("520")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Tecnologico datosTec = dataSnapshot.getValue(Tecnologico.class);
                            TextView textView = findViewById(R.id.tvPrincipal);
                            textView.setText(datosTec.getNombre());
                        } else {
                            Toast.makeText(getApplicationContext(), "No existe el instituto con clave 520", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void abrirRegistarPlanes(View view) {
        //Intent i = new Intent(this, Plan_de_estudios.class);
        Intent i = new Intent(this, Lista_plan_estudios.class);
        startActivity(i);
    }
}
