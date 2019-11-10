package com.example.controlescolar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //Prueba para cambiar el texto por el nombre del instituto







        setContentView(R.layout.activity_principal);




        final Fragment tec = new FragmentoTecnologico();

        if(savedInstanceState == null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, tec).commit();
        }

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navegacion);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment este = null;

                if(menuItem.getItemId() == R.id.tecnologico){
                    este = new FragmentoTecnologico();

                }else if(menuItem.getItemId() == R.id.empleados){
                    este = new FragmentoEmpleados();

                }else if (menuItem.getItemId() == R.id.materias){
                    este = new FragmentoMaterias();

                }else if (menuItem.getItemId() == R.id.grupos){
                    este = new FragmentoGrupos();

                }else if (menuItem.getItemId() == R.id.estudiantes){
                    este = new FragmentoEstudiantes();

                }

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.container, este).commit();


                return true;
            }
        });


    }
}
