package com.example.controlescolar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.controlescolar.POJO.PlanE;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Plan_de_estudios extends AppCompatActivity {
    private EditText clavePlan_uno, nombre_plan;
    private EditText clavePlan_tres1;
    private DatabaseReference mDatabase;
  //  private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_de_estudios);
        initVariables();

    }


    public void guardarPlan(final View view) {
        if (validarCamposVacios()) {
            final PlanE p = new PlanE();
           final String clave = clavePlan_uno.getText().toString();

            p.setNombre(nombre_plan.getText().toString());

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.progressbar_general);
            final Dialog dialog = builder.create();
            dialog.show();

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mDatabase.child("planesdeestudio").child(clave).setValue(p);
                    dialog.dismiss();
                    limpiarCampos();
                    InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Snackbar.make(getCurrentFocus(), "Exito!!", Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    dialog.dismiss();
                    Snackbar.make(getCurrentFocus(), "Ha ocurrido un error\nIntentelo mas tarde", Snackbar.LENGTH_SHORT).show();
                }
            });

        }
    }

    public Boolean validarCamposVacios() {
        if (clavePlan_uno.getText().toString().isEmpty()) {
            clavePlan_uno.setError("Requerido");
            return false;
        }

        if (nombre_plan.getText().toString().isEmpty()) {
            nombre_plan.setError("Requerido");
            return false;
        }

        return true;
    }

    public void initVariables() {
        clavePlan_uno = findViewById(R.id.tv_claveUno);
        nombre_plan = findViewById(R.id.tv_nombre_plan);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void limpiarCampos() {
        clavePlan_uno.setText(null);
        nombre_plan.setText(null);
    }
}
