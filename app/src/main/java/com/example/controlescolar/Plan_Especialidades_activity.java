package com.example.controlescolar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.controlescolar.POJO.EspecialidadE;
import com.example.controlescolar.POJO.PlanE;
import com.example.controlescolar.RecyclerView.PlanesAdaptador;
import com.example.controlescolar.RecyclerView.Planes_Especialidades_Adaptador;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Plan_Especialidades_activity extends Fragment {
    private TextView keyPlan, namePlan;
    private DatabaseReference databaseReference;
    private ArrayList<EspecialidadE> lista_especialidades;
    private String keyPlanStr;
    private RecyclerView recyclerView;
    public View v;

    public Plan_Especialidades_activity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_plan__especialidades_activity, container, false);

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        iniciarComponentes();

        return v;

    }
    //@Override
    //protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
  //      setContentView(R.layout.activity_plan__especialidades_activity);
    //    iniciarComponentes();
    //}

    public void iniciarAdaptador() {

        Planes_Especialidades_Adaptador adaptador = new Planes_Especialidades_Adaptador(lista_especialidades,  getActivity());
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
                Snackbar.make(getActivity().getCurrentFocus(), "Error al cargar la base de datos: " + databaseError.toException(), Snackbar.LENGTH_SHORT).show();
            }
        });


    }

    public void iniciarComponentes() {
        keyPlan = getActivity().findViewById(R.id.key_planDeEstudio_PE);
        namePlan = getActivity().findViewById(R.id.nombre_planDeEstudio_PE);
        lista_especialidades = new ArrayList<>();
        keyPlanStr = "";
        FirebaseApp.initializeApp(getActivity());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView =getActivity().findViewById(R.id.miRecicleyViewPlanesEspecialidades);
    }
}
