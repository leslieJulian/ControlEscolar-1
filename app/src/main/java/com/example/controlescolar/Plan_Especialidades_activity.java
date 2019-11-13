package com.example.controlescolar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.controlescolar.POJO.EspecialidadE;
import com.example.controlescolar.POJO.PlanE;
import com.example.controlescolar.RecyclerView.PlanesAdaptador;
import com.example.controlescolar.RecyclerView.Planes_Especialidades_Adaptador;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Plan_Especialidades_activity extends AppCompatActivity {
    private TextView keyPlan, namePlan;
    private DatabaseReference databaseReference;
    private ArrayList<EspecialidadE> lista_especialidades;
    private String keyPlanStr;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan__especialidades_activity);
        iniciarComponentes();
    }

    public void iniciarAdaptador() {

        Planes_Especialidades_Adaptador adaptador = new Planes_Especialidades_Adaptador(lista_especialidades, Plan_Especialidades_activity.this);
        recyclerView.setAdapter(adaptador);

    }

    public void iniciarLista() {
        databaseReference.child("planesdeestudio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_especialidades.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    EspecialidadE c = d.getValue(EspecialidadE.class);

                    lista_especialidades.add(c);
                }
                iniciarAdaptador();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snackbar.make(Plan_Especialidades_activity.this.getCurrentFocus(), "Error al cargar la base de datos: " + databaseError.toException(), Snackbar.LENGTH_SHORT).show();
            }
        });


    }

    public void iniciarComponentes() {
        keyPlan = findViewById(R.id.key_planDeEstudio_PE);
        namePlan = findViewById(R.id.nombre_planDeEstudio_PE);
        lista_especialidades = new ArrayList<>();
        keyPlanStr = "";
        FirebaseApp.initializeApp(Plan_Especialidades_activity.this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.miRecicleyViewPlanesEspecialidades);
    }
}
