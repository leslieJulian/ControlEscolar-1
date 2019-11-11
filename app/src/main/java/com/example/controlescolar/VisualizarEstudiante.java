package com.example.controlescolar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class VisualizarEstudiante extends Fragment {

    EditText numeroControl, nombre, primerapellido, segundoapellido;
    TextView periodo;
    Button btnBuscar, btnRegresar, btnGuardar;
    ArrayList<String> arregloClavesPlanes, arregloPlanes, arrayClavesEspecialidades, arrayEspecialidades;
    ArrayAdapter adaptadorPlanes, adaptadorEspecialidades;
    Spinner spinnerPlanesEstudio, spinnerEspecialidades;
    String periodoRegistrado;

    View v;

    public VisualizarEstudiante(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_visualizar_estudiante, container, false);

        //setContentView(R.layout.activity_visualizar_estudiante);
        numeroControl = v.findViewById(R.id.etNumeroControl);
        btnBuscar = v.findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(buscar);
        btnRegresar = v.findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(regresar);
        btnGuardar = v.findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(guardar);
        spinnerEspecialidades = v.findViewById(R.id.spinnerEspecialidad);
        spinnerPlanesEstudio = v.findViewById(R.id.spinnerPlanEstudios);
        spinnerPlanesEstudio.setOnItemSelectedListener(getEspecialidades);
        nombre = v.findViewById(R.id.etNombre);
        primerapellido = v.findViewById(R.id.etPrimerApellido);
        segundoapellido = v.findViewById(R.id.etSegundoApellido);
        periodo = v.findViewById(R.id.tvPeriodo);
        getPlanesEstudio();

        return v;
    }

    //Métodos de recuperación de datos en FireBase
    public void getPlanesEstudio(){
        //Inicializando arreglos
        arregloPlanes = new ArrayList<>();
        arregloClavesPlanes = new ArrayList<>();
        //Inicializando y agregando el adaptador al spinner
        adaptadorPlanes = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arregloPlanes);
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
                        Toast.makeText(getActivity().getApplicationContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    AdapterView.OnItemSelectedListener getEspecialidades = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //Inicializando el arreglo
            arrayEspecialidades = new ArrayList<>();
            arrayClavesEspecialidades = new ArrayList<>();
            //Obteniendo la referencia al nodo de planesdeestudio
            FirebaseDatabase.getInstance().getReference().child("especialidades")
                    //Añadiendo en listener a la referencia
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                //Obteniendo el objeto y recuperando el nombre y clave de cada plan
                                Especialidad especialidad = snapshot.getValue(Especialidad.class);
                                if(especialidad.getEstado().equals("true")) {
                                    //Si la especialidad es correspondiente al plan seleccionado
                                    if (arregloClavesPlanes.get(spinnerPlanesEstudio.getSelectedItemPosition()).equals(especialidad.getPlan())) {
                                        arrayClavesEspecialidades.add(snapshot.getKey());
                                        arrayEspecialidades.add(especialidad.getNombre());
                                    }
                                }
                            }
                            //Inicializando y agregando el adaptador al spinner
                            adaptadorEspecialidades = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, arrayEspecialidades);
                            spinnerEspecialidades.setAdapter(adaptadorEspecialidades);
                            adaptadorEspecialidades.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            Toast.makeText(getActivity().getApplicationContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    //ClickListeners
    View.OnClickListener buscar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Consulta a la BD del número de control al nodo estudiantes
            FirebaseDatabase.getInstance().getReference().child("estudiantes").child(numeroControl.getText().toString())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //Verificamos que el estudiante existe
                            if(dataSnapshot.exists() && !numeroControl.getText().toString().equals("")) {
                                Estudiante estudiante = dataSnapshot.getValue(Estudiante.class);
                                nombre.setText(estudiante.getNombre());
                                primerapellido.setText(estudiante.getPrimerapellido());
                                segundoapellido.setText(estudiante.getSegundoapellido());
                                for(int i=0; i<arregloClavesPlanes.size(); i++) {
                                    if(arregloClavesPlanes.get(i).equals(estudiante.getPlan())) {
                                        spinnerPlanesEstudio.setSelection(i);
                                    }
                                }
                                for(int i=0; i<arrayClavesEspecialidades.size(); i++) {
                                    if(arrayClavesEspecialidades.get(i).equals(estudiante.getEspecialidad())) {
                                        spinnerEspecialidades.setSelection(i);
                                    }
                                }
                                periodoRegistrado = estudiante.getPeriodo();
                                String periodoNum = estudiante.getPeriodo().substring(4,estudiante.getPeriodo().length());
                                if(periodoNum.equals("1")){
                                    periodo.setText("Periodo de inscripción: ENE-FEB/"+estudiante.getPeriodo().substring(0,estudiante.getPeriodo().length()-1));
                                }else if(periodoNum.equals("2")){
                                    periodo.setText("Periodo de inscripción: VERANO/"+estudiante.getPeriodo().substring(0,estudiante.getPeriodo().length()-1));
                                }else if(periodoNum.equals("3")){
                                    periodo.setText("Periodo de inscripción: AGO-DIC/"+estudiante.getPeriodo().substring(0,estudiante.getPeriodo().length()-1));
                                }
                            }else{
                                nombre.setText("");
                                Toast.makeText(getActivity().getApplicationContext(), "No existe este número de control", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    };

    View.OnClickListener regresar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment este = null;
            este = new FragmentoEstudiantes();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.container, este).commit();
        }
    };

    View.OnClickListener guardar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Obteniendo los datos a registrar
            EditText nombre = v.findViewById(R.id.etNombre);
            EditText primerapellido = v.findViewById(R.id.etPrimerApellido);
            EditText segundoapellido = v.findViewById(R.id.etSegundoApellido);
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
                                    FirebaseDatabase.getInstance().getReference().child("estudiantes").child(numeroControl.getText().toString()).setValue(new Estudiante(nombre.getText().toString(), primerapellido.getText().toString(), segundoapellido.getText().toString(), periodoRegistrado, arregloClavesPlanes.get(spinnerPlanesEstudio.getSelectedItemPosition()).toString(), arrayClavesEspecialidades.get(spinnerEspecialidades.getSelectedItemPosition()).toString()));
                                    //Limpiando los campos
                                    nombre.setText("");
                                    primerapellido.setText("");
                                    segundoapellido.setText("");
                                    //Mensaje
                                    Toast.makeText(getActivity().getApplicationContext(), "Se han actualizado los datos correctamente", Toast.LENGTH_SHORT).show();
                                    //Regresamos al menu principal olvidandonos del historial de activities en la app

                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    Fragment este = null;
                                    este = new FragmentoEstudiantes();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                    fragmentTransaction.replace(R.id.container, este).commit();

                                }else{
                                    Toast.makeText(getActivity().getApplicationContext(), "Caracteres inválidos en 'Segundo apellido'", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getActivity().getApplicationContext(), "Segundo apellido vacío", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(), "Caracteres inválidos en 'Primer apellido'", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(), "Primer apellido vacío", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Caracteres inválidos en 'Nombre'", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getActivity().getApplicationContext(), "Nombre vacío", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
