package com.example.controlescolar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrarEstudiante extends AppCompatActivity {
    //Variables locales
    String numeroControl, periodoRegistro;
    ArrayList<String> arregloPlanes;    ArrayList<String> arregloClavesPlanes;
    ArrayList<String> arregloEspecialidades;    ArrayList<String> arregloClavesEspecialidades;
    ArrayAdapter adaptadorPlanes, adaptadorEspecialidades;
    Button btnGuardar, btnCancelar;
    Spinner spinnerPlanesEstudio, spinnerEspecialidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_estudiante);
        //Inicializando los elementos
        btnGuardar = (Button)findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(guardar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(cancelar);
        spinnerEspecialidades = findViewById(R.id.spinnerEspecialidad);
        spinnerPlanesEstudio = findViewById(R.id.spinnerPlanEstudios);
        spinnerPlanesEstudio.setOnItemSelectedListener(getEspecialidades);
        getPlanesEstudio();
        generarNumeroControl();
    }


    //Métodos de recuperación de datos de FireBase
    public void getPlanesEstudio(){
        //Inicializando el arreglo
        arregloPlanes = new ArrayList<>();
        arregloClavesPlanes = new ArrayList<>();
        //Inicializando y agregando el adaptador al spinner
        adaptadorPlanes = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arregloPlanes);
        spinnerPlanesEstudio.setAdapter(adaptadorPlanes);
        //Obteniendo la referencia al nodo de planesdeestudio
        FirebaseDatabase.getInstance().getReference().child("planesdeestudio")
                //Añadiendo en listener a la referencia
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //Obteniendo el objeto y recuperando el nombre y clave de cada plan
                            PlanEstudio planEstudio = snapshot.getValue(PlanEstudio.class);
                            arregloClavesPlanes.add(snapshot.getKey());
                            arregloPlanes.add(planEstudio.getNombre());
                        }
                        adaptadorPlanes.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    AdapterView.OnItemSelectedListener getEspecialidades = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //Inicializando el arreglo
            arregloEspecialidades = new ArrayList<>();
            arregloClavesEspecialidades = new ArrayList<>();
            //Obteniendo la referencia al nodo de planesdeestudio
            FirebaseDatabase.getInstance().getReference().child("especialidades")
                    //Añadiendo en listener a la referencia
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                //Obteniendo el objeto y recuperando el nombre y clave de cada plan
                                Especialidad especialidad = snapshot.getValue(Especialidad.class);
                                //Si la especialidad es correspondiente al plan seleccionado
                                if(arregloClavesPlanes.get(spinnerPlanesEstudio.getSelectedItemPosition()).equals(especialidad.getPlan())) {
                                    arregloClavesEspecialidades.add(snapshot.getKey());
                                    arregloEspecialidades.add(especialidad.getNombre());
                                }
                            }
                            adaptadorEspecialidades = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arregloEspecialidades);
                            //Inicializando y agregando el adaptador al spinner
                            spinnerEspecialidades.setAdapter(adaptadorEspecialidades);
                            adaptadorEspecialidades.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };




    public void generarNumeroControl(){
        getPeriodo();
    }
    public void getPeriodo(){
        //Obteniendo la referencia al nodo de tecnologicos
        FirebaseDatabase.getInstance().getReference().child("datos")
                //Añadiendo en listener a la referencia
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //Obteniendo el objeto y recuperando el periodo
                            Tecnologico tecnologico = snapshot.getValue(Tecnologico.class);
                            //Si es el tec de chilpancingo
                            if(snapshot.getKey().toString().equals("520")) {
                                //Obtenemos el periodo actual
                                periodoRegistro = tecnologico.getPeriodoactual();
                                getNumeroEstudiante(tecnologico.getPeriodoactual(),tecnologico.getPeriodoactual().substring(2, tecnologico.getPeriodoactual().length() - 1));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void getNumeroEstudiante(String periodoactual, final String digitosPeriodo){
        //Obteniendo la referencia al último nodo Estudiantes del periodo actual
        FirebaseDatabase.getInstance().getReference().child("estudiantes").orderByChild("periodo").limitToLast(1).equalTo(periodoactual)
                //Añadiendo en listener a la referencia
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                //Obteniendo el objeto y recuperando ultimos tres digitos
                                Estudiante estudiante = snapshot.getValue(Estudiante.class);
                                int numero = Integer.parseInt(snapshot.getKey().substring(5, snapshot.getKey().length()));
                                //Agregamos los ceros a cada numero de control según el caso
                                if(numero <= 8){
                                    numeroControl = digitosPeriodo+"52000"+(numero+1);
                                }else if(numero <= 98){
                                    numeroControl = digitosPeriodo+"5200"+(numero+1);
                                }else{
                                    numeroControl = digitosPeriodo+"520"+(numero+1);
                                }
                            }
                        }else{
                            numeroControl = digitosPeriodo+"520001";
                            Toast.makeText(getApplicationContext(), numeroControl, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //ClickListener
    View.OnClickListener cancelar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Regresamos al menu principal olvidandonos del historial de activities en la app
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };
    View.OnClickListener guardar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Obteniendo los datos a registrar
            EditText nombre = findViewById(R.id.etNombre);
            EditText primerapellido = findViewById(R.id.etPrimerApellido);
            EditText segundoapellido = findViewById(R.id.etSegundoApellido);
            //Validando
            if(!nombre.getText().toString().equals("")){
                //Comparando valor con regex
                if(Pattern.compile("[(A-ZÁ-Úa-zá-ú)*\\s*]+").matcher(nombre.getText().toString()).matches()){
                    if(!primerapellido.getText().toString().equals("")){
                        //Comparando valor con regex
                        if(Pattern.compile("[(A-ZÁ-Úa-zá-ú)*\\s*]+").matcher(primerapellido.getText().toString()).matches()){
                            if(!segundoapellido.getText().toString().equals("")){
                                //Comparando valor con regex
                                if(Pattern.compile("[(A-ZÁ-Úa-zá-ú)*\\s*]+").matcher(segundoapellido.getText().toString()).matches()){
                                    //Guardando los datos en el nodo estudiantes
                                    FirebaseDatabase.getInstance().getReference().child("estudiantes").child(numeroControl).setValue(new Estudiante(nombre.getText().toString(), primerapellido.getText().toString(), segundoapellido.getText().toString(), periodoRegistro, arregloClavesPlanes.get(spinnerPlanesEstudio.getSelectedItemPosition()).toString(), arregloClavesEspecialidades.get(spinnerEspecialidades.getSelectedItemPosition()).toString()));
                                    //Limpiando los campos
                                    nombre.setText("");
                                    primerapellido.setText("");
                                    segundoapellido.setText("");
                                    //Mensaje
                                    Toast.makeText(getApplicationContext(), "Se ha registrado al estudiante correctamente", Toast.LENGTH_SHORT).show();
                                    //Regresamos al menu principal olvidandonos del historial de activities en la app
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Caracteres inválidos en 'Segundo apellido'", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Segundo apellido vacío", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Caracteres inválidos en 'Primer apellido'", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Primer apellido vacío", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Caracteres inválidos en 'Nombre'", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Nombre vacío", Toast.LENGTH_SHORT).show();
            }
        }
    };
}


