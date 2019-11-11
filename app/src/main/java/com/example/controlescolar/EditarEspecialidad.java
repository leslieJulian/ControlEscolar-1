package com.example.controlescolar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controlescolar.POJO.EspecialidadE;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

public class EditarEspecialidad extends AppCompatActivity {
    private String key;
    private EspecialidadE especialidad;
    private TextView tvtplan_estudios;
    private EditText tvtespecialidad;
    private Switch btn_estado;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_especialidad);

        inicializarVariables();
        cargarDatos();
    }

    public void guardarEspecialidadEdicion(final View view) {
        final EspecialidadE especialidadEditada = new EspecialidadE();
        especialidadEditada.setEstado(btn_estado.isChecked() + "");
        especialidadEditada.setNombre(tvtespecialidad.getText().toString());
        especialidadEditada.setPlan(especialidad.getPlan());
        if (validarEspecialidad()) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mDatabase.child("especialidades").child(key).setValue(especialidadEditada);
                    InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Snackbar.make(getCurrentFocus(), "Exito!!", Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Snackbar.make(getCurrentFocus(), "Ha ocurrido un error\nIntentelo mas tarde", Snackbar.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void regresar(View view) {
        Intent c = new Intent(this, Lista_especialidades_act.class);
        startActivity(c);
    }

    public boolean validarEspecialidad() {
        String esp = tvtespecialidad.getText().toString();
        if (esp.isEmpty() || esp == null) {
            tvtplan_estudios.setError("Requerido");
            return false;
        }
        return true;
    }

    private void inicializarVariables() {
        key = getIntent().getExtras().getString("key");
        especialidad = (EspecialidadE) getIntent().getSerializableExtra("especialidad");
        tvtespecialidad = findViewById(R.id.tv_nombre_especialidadEditar);
        tvtplan_estudios = findViewById(R.id.tvt_plan_estudiosEspecialidadEditar);
        btn_estado = findViewById(R.id.switchEditarEspecialidad);
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    public void cargarDatos() {
        tvtplan_estudios.setText(especialidad.getPlan().toString());
        tvtespecialidad.setText(especialidad.getNombre().toString());
        if (especialidad.getEstado().toString().equalsIgnoreCase("true")) {
            btn_estado.setChecked(true);
        } else {
            btn_estado.setChecked(false);
        }
    }
}
