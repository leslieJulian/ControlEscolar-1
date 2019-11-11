package com.example.controlescolar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

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
import java.util.UUID;

public class Especialidad_act extends AppCompatActivity {
    private List<String> lista_planes;

    private List<Planesin> lista_planesKey;
    private DatabaseReference databaseReference;
    private Spinner mispinner;
    private Switch estado_especialidad;

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

                    Planesin pp = new Planesin(c.getNombre(), d.getKey().toString());

                    lista_planesKey.add(pp);
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

    public void guardarEspecialidad(final View view) {
        if (validarCampos()) {
            final EspecialidadE especialidad = new EspecialidadE();
            especialidad.setNombre(edt_especialidad.getText().toString());
            especialidad.setPlan(obtnerKeydelPlanDeEstudios(mispinner.getSelectedItem().toString()));
            especialidad.setEstado(estado_especialidad.isChecked() + "");
            final String claveChild = generarClave(especialidad);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    databaseReference.child("especialidades").child(claveChild).setValue(especialidad);

                    limpiarCampos();
                    InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Snackbar.make(getCurrentFocus(), "Subido Correctamente", Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Snackbar.make(getCurrentFocus(), "Ha ocurrido un error\nIntentelo mas tarde", Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    public String generarClave(EspecialidadE especialidad) {

        String homoclave = UUID.randomUUID().toString();

        String clave = "asd-asda";
        String[] homoclavelista = homoclave.split("-");
        String[] clavedelaespecialidad = obtnerKeydelPlanDeEstudios(especialidad.getPlan().toString()).split("-");
        Toast.makeText(this, "Este es " + especialidad.getNombre().toString(), Toast.LENGTH_LONG).show();
        clave = clavedelaespecialidad[0] + "-" + homoclavelista[0];

        return clave;
    }

    public void limpiarCampos() {
        mispinner.setSelection(1);
        edt_especialidad.setText(null);
    }

    public String obtnerKeydelPlanDeEstudios(String nombre_plan) {
        for (Planesin p : lista_planesKey) {
            if (p.getNombre().equalsIgnoreCase(nombre_plan)) {
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

        estado_especialidad = findViewById(R.id.switch2);
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
