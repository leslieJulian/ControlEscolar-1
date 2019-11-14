package com.example.controlescolar;

import android.os.Bundle;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Lista_plan_estudios extends Fragment {
    private ArrayList<PlanE> lista_planes;
    private ArrayList<String> listaKeys;
    private DatabaseReference databaseReference;
    public View v;
    private RecyclerView recyclerView;

    public Lista_plan_estudios() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_lista_plan_estudios, container, false);

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        inicialrComponentes();

        return v;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    public void iniciarAdaptador() {

        PlanesAdaptador adaptador = new PlanesAdaptador(lista_planes, getActivity(), listaKeys);
        recyclerView.setAdapter(adaptador);

    }

    public void iniciarLista() {
        databaseReference.child("planesdeestudio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_planes.clear();
                listaKeys.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    PlanE c = d.getValue(PlanE.class);
                    lista_planes.add(c);
                    listaKeys.add(d.getKey().toString());
                }
                iniciarAdaptador();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snackbar.make(getActivity().getCurrentFocus(), "Error al cargar la base de datos: " + databaseError.toException(), Snackbar.LENGTH_SHORT).show();
            }
        });


    }


    public void inicialrComponentes() {
        FirebaseApp.initializeApp(getActivity());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) v.findViewById(R.id.miRecicleyView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        lista_planes = new ArrayList<>();
        listaKeys = new ArrayList<>();
        iniciarLista();
    }

}