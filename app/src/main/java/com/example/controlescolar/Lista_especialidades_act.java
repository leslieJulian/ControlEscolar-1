package com.example.controlescolar;

import android.os.Bundle;

import com.example.controlescolar.POJO.EspecialidadE;
import com.example.controlescolar.POJO.PlanE;
import com.example.controlescolar.RecyclerView.EspecialidadesAdaptador;
import com.example.controlescolar.RecyclerView.PlanesAdaptador;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;

public class Lista_especialidades_act extends AppCompatActivity {
    private ArrayList<EspecialidadE> lista_especialidades;
    private DatabaseReference databaseReference;
    private ArrayList<String> lista_keys;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_especialidades_act);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        inicialrComponentes();


    }

    public void iniciarAdaptador() {
        EspecialidadesAdaptador adaptador = new EspecialidadesAdaptador(lista_especialidades, Lista_especialidades_act.this,lista_keys);
        recyclerView.setAdapter(adaptador);

    }

    public void iniciarLista() {
        databaseReference.child("especialidades").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_especialidades.clear();
                lista_keys.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    EspecialidadE c = d.getValue(EspecialidadE.class);
                    lista_especialidades.add(c);
                    lista_keys.add(d.getKey());
                }
                iniciarAdaptador();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snackbar.make(Lista_especialidades_act.this.getCurrentFocus(), "Error al cargar la base de datos: " + databaseError.toException(), Snackbar.LENGTH_SHORT).show();
            }
        });


    }


    public void inicialrComponentes() {
        FirebaseApp.initializeApp(Lista_especialidades_act.this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) findViewById(R.id.miRecicleyViewEspecialidades);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Lista_especialidades_act.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        lista_especialidades = new ArrayList<>();
        lista_keys = new ArrayList<>();
        iniciarLista();
    }


}