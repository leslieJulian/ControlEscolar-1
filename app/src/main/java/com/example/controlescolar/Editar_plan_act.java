package com.example.controlescolar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controlescolar.POJO.PlanE;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Editar_plan_act extends AppCompatActivity {
    private TextView clave_plan;
    private EditText nombre_plan;
    private String nombrePlan;
    private String keyPlan;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_plan_act);
        inicarComponentes();
        llenarCamposAEditar();
        Toast.makeText(this, keyPlan, Toast.LENGTH_LONG).show();

    }

    public void guardarEdicion(final View view) {
        if (validarCampos()) {
            final PlanE planEdicion = new PlanE();
            planEdicion.setNombre(nombre_plan.getText().toString());
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mDatabase.child("planesdeestudio").child(keyPlan).setValue(planEdicion);
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

    public void inicarComponentes() {
        nombrePlan = getIntent().getExtras().getString("nplan");
        keyPlan = getIntent().getExtras().getString("keyplan");
        clave_plan = findViewById(R.id.tv_claveUnoEditarr);
        nombre_plan = findViewById(R.id.tv_nombre_planEditarr);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void llenarCamposAEditar() {
        clave_plan.setText(keyPlan);
        nombre_plan.setText(nombrePlan);
    }

    public boolean validarCampos() {
        if (nombre_plan.getText().toString().isEmpty() || nombre_plan.getText().toString() == null) {
            nombre_plan.setError("Requerido");
            return false;
        }
        return true;
    }
}
