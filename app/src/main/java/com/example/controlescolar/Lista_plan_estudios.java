package com.example.controlescolar;

import android.os.Bundle;

import com.example.controlescolar.POJO.PlanE;
import com.example.controlescolar.RecyclerView.EspecialidadesAdaptador;
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
import java.util.List;

public class Lista_plan_estudios extends AppCompatActivity {
    private ArrayList<PlanE> lista_planes;
    private DatabaseReference databaseReference;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_plan_estudios);
        //Toolbar toolbar = findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

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
        EspecialidadesAdaptador adaptador = new EspecialidadesAdaptador(lista_planes, Lista_plan_estudios.this);
        recyclerView.setAdapter(adaptador);

    }

    public void iniciarLista() {
        databaseReference.child("planesdeestudio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_planes.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    PlanE c = d.getValue(PlanE.class);
                    lista_planes.add(c);
                }
                iniciarAdaptador();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snackbar.make(Lista_plan_estudios.this.getCurrentFocus(), "Error al cargar la base de datos: " + databaseError.toException(), Snackbar.LENGTH_SHORT).show();
            }
        });


    }


    public void inicialrComponentes() {
        FirebaseApp.initializeApp(Lista_plan_estudios.this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) findViewById(R.id.miRecicleyView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Lista_plan_estudios.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        lista_planes = new ArrayList<>();
        iniciarLista();
    }
}