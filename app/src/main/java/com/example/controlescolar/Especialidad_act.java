package com.example.controlescolar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.controlescolar.POJO.EspecialidadE;
import com.example.controlescolar.POJO.PlanE;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Especialidad_act extends AppCompatActivity {
    private List<String> lista_planes;
    private List<Planesin> lista_planesKey;
    private DatabaseReference databaseReference;
    private Spinner mispinner;
    private DatabaseReference mDatabase;
    private Button btn_registar;
    private EditText edt_especialidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_especialidad_act);
        inicialrComponentes();
        iniciarSpinner();
    }

    public void iniciarSpinner() {
        lista_planes.clear();
        databaseReference.child("planesdeestudio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                limpiarArreglos();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    PlanE c = d.getValue(PlanE.class);
                    lista_planes.add(c.getNombre());
                    lista_planesKey.add(new Planesin(c.getNombre(), d.getKey()));
                }
                if (lista_planes.isEmpty()) {
                    btn_registar.setEnabled(false);
                } else {
                    mispinner.setAdapter(new ArrayAdapter<String>(Especialidad_act.this, android.R.layout.simple_spinner_item, lista_planes));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snackbar.make(Especialidad_act.this.getCurrentFocus(), "Error al cargar la base de datos: " + databaseError.toException(), Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    public void limpiarArreglos() {
        lista_planes.clear();
        lista_planesKey.clear();

    }

    public void guardarEspecialidad(View view) {
        if (validarCampos()) {
            EspecialidadE especialidad = new EspecialidadE();
            especialidad.setNombre_especialidad(edt_especialidad.getText().toString());

        }
    }

    public String obtnerKeydelPlanDeEstudios(String nombre_plan) {
        for (Planesin p : lista_planesKey) {
            if (p.key.equalsIgnoreCase(nombre_plan)) {
                return p.getKey();
            }
        }

        return nombre_plan;
    }

 

    public Boolean validarCampos() {
        if (edt_especialidad.getText().toString().isEmpty()) {
            edt_especialidad.setError("Requerido");
            return false;
        }
        return true;
    }

    public void inicialrComponentes() {
        FirebaseApp.initializeApp(Especialidad_act.this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        lista_planes = new ArrayList<>();
        mispinner = findViewById(R.id.sppiner_clave_plan);
        btn_registar = findViewById(R.id.btn_registrar_especialidad);
        edt_especialidad = findViewById(R.id.tv_nombre_especialidad);
        lista_planesKey = new ArrayList<>();
    }

    public class Planesin {
        private String nombre;
        private String key;

        public String getNombre() {
            return nombre;
        }

        public Planesin(String nombre, String key) {
            this.nombre = nombre;
            this.key = key;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

    }
}
