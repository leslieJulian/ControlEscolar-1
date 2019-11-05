package com.example.controlescolar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FragmentHorario extends Fragment {
    static Activity activity;
    static View v;
    public static ArrayList<String> claves = new ArrayList<String>();
    public static ArrayList<String> materiasCargadas = new ArrayList<String>();
    static Button guardar;

    public FragmentHorario(Activity activity){
        this.activity = activity;
    }
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ver_horario, container, false);
        guardar = v.findViewById(R.id.btnGuardarHorario);
        guardar.setOnClickListener(guardarHorario);
        cargarHorario();
        return v;
    }

    public static void cargarHorario(){
        limpiarCampos();guardar.setEnabled(true);
        //Validamos num de control
        if(!FragmentCargarMaterias.numControlString.equals("")){
        //Verificamos que el alumno existe
        FirebaseDatabase.getInstance().getReference().child("estudiantes").child(FragmentCargarMaterias.numControlString)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            //Guardamos su periodo de registro
                            Estudiante estudiante = dataSnapshot.getValue(Estudiante.class);
                            //Verificamos que el estudiante aun no tenga materias cargadas para este periodo
                            FirebaseDatabase.getInstance().getReference().child("seleccionmaterias").orderByChild("estudiante").equalTo(FragmentCargarMaterias.numControlString)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            boolean valor = true;
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                String[] elementos = snapshot.getKey().split("%");
                                                //Si existen materias seleccionadas en el periodo actual por el estudiante
                                                //No se permiten cargar nuevamente
                                                if (elementos[3].equals(FragmentCargarMaterias.periodoActual)) {
                                                    valor = false;
                                                    break;
                                                }
                                            }
                                            //Si aun no ha cargado materias el estudiante, se procede a mostrar el horario segun sus elecciones
                                            if (valor) {
                                                //Recorremos cada materia seleccionada y sus datos los colocamos
                                                //en los dias correspondientes
                                                for(int i=0; i<claves.size(); i+=2){
                                                    for(int k=0; k<FragmentCargarMaterias.materias.size(); k++){
                                                        //Obteniendo los datos
                                                        if((claves.get(i).equals(FragmentCargarMaterias.materias.get(k).getClave()))&&(claves.get(i+1).equals(FragmentCargarMaterias.materias.get(k).getHora()))){
                                                            //Según su horario
                                                            String clase = FragmentCargarMaterias.materias.get(k).getNombre()+", \nAula: "+FragmentCargarMaterias.materias.get(k).getAula();
                                                            switch(FragmentCargarMaterias.materias.get(k).getHora()){
                                                                case "07:00":
                                                                    TextView dia = v.findViewById(R.id.lunes7a8); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.martes7a8); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.miercoles7a8); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.jueves7a8); dia.setText(clase);
                                                                    if(Integer.parseInt(FragmentCargarMaterias.materias.get(k).getCreditos()) > 4){
                                                                        dia = v.findViewById(R.id.viernes7a8); dia.setText(clase);
                                                                    }
                                                                    break;
                                                                case "08:00":
                                                                    dia = v.findViewById(R.id.lunes8a9); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.martes8a9); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.miercoles8a9); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.jueves8a9); dia.setText(clase);
                                                                    if(Integer.parseInt(FragmentCargarMaterias.materias.get(k).getCreditos()) > 4){
                                                                        dia = v.findViewById(R.id.viernes8a9); dia.setText(clase);
                                                                    }
                                                                    break;
                                                                case "09:00":
                                                                    dia = v.findViewById(R.id.lunes9a10); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.martes9a10); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.miercoles9a10); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.jueves9a10); dia.setText(clase);
                                                                    if(Integer.parseInt(FragmentCargarMaterias.materias.get(k).getCreditos()) > 4){
                                                                        dia = v.findViewById(R.id.viernes9a10); dia.setText(clase);
                                                                    }
                                                                    break;
                                                                case "10:00":
                                                                    dia = v.findViewById(R.id.lunes10a11); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.martes10a11); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.miercoles10a11); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.jueves10a11); dia.setText(clase);
                                                                    if(Integer.parseInt(FragmentCargarMaterias.materias.get(k).getCreditos()) > 4){
                                                                        dia = v.findViewById(R.id.viernes10a11); dia.setText(clase);
                                                                    }
                                                                    break;
                                                                case "11:00":
                                                                    dia = v.findViewById(R.id.lunes11a12); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.martes11a12); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.miercoles11a12); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.jueves11a12); dia.setText(clase);
                                                                    if(Integer.parseInt(FragmentCargarMaterias.materias.get(k).getCreditos()) > 4){
                                                                        dia = v.findViewById(R.id.viernes11a12); dia.setText(clase);
                                                                    }
                                                                    break;
                                                                case "12:00":
                                                                    dia = v.findViewById(R.id.lunes12a13); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.martes12a13); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.miercoles12a13); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.jueves12a13); dia.setText(clase);
                                                                    if(Integer.parseInt(FragmentCargarMaterias.materias.get(k).getCreditos()) > 4){
                                                                        dia = v.findViewById(R.id.viernes12a13); dia.setText(clase);
                                                                    }
                                                                    break;
                                                                case "13:00":
                                                                    dia = v.findViewById(R.id.lunes13a14); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.martes13a14); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.miercoles13a14); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.jueves13a14); dia.setText(clase);
                                                                    if(Integer.parseInt(FragmentCargarMaterias.materias.get(k).getCreditos()) > 4){
                                                                        dia = v.findViewById(R.id.viernes13a14); dia.setText(clase);
                                                                    }
                                                                    break;
                                                                case "14:00":
                                                                    dia = v.findViewById(R.id.lunes14a15); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.martes14a15); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.miercoles14a15); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.jueves14a15); dia.setText(clase);
                                                                    if(Integer.parseInt(FragmentCargarMaterias.materias.get(k).getCreditos()) > 4){
                                                                        dia = v.findViewById(R.id.viernes14a15); dia.setText(clase);
                                                                    }
                                                                    break;
                                                                case "15:00":
                                                                    dia = v.findViewById(R.id.lunes15a16); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.martes15a16); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.miercoles15a16); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.jueves15a16); dia.setText(clase);
                                                                    if(Integer.parseInt(FragmentCargarMaterias.materias.get(k).getCreditos()) > 4){
                                                                        dia = v.findViewById(R.id.viernes15a16); dia.setText(clase);
                                                                    }
                                                                    break;
                                                                case "16:00":
                                                                    dia = v.findViewById(R.id.lunes16a17); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.martes16a17); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.miercoles16a17); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.jueves16a17); dia.setText(clase);
                                                                    if(Integer.parseInt(FragmentCargarMaterias.materias.get(k).getCreditos()) > 4){
                                                                        dia = v.findViewById(R.id.viernes16a17); dia.setText(clase);
                                                                    }
                                                                    break;
                                                                case "17:00":
                                                                    dia = v.findViewById(R.id.lunes17a18); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.martes17a18); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.miercoles17a18); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.jueves17a18); dia.setText(clase);
                                                                    if(Integer.parseInt(FragmentCargarMaterias.materias.get(k).getCreditos()) > 4){
                                                                        dia = v.findViewById(R.id.viernes17a18); dia.setText(clase);
                                                                    }
                                                                    break;
                                                                case "18:00":
                                                                    dia = v.findViewById(R.id.lunes18a19); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.martes18a19); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.miercoles18a19); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.jueves18a19); dia.setText(clase);
                                                                    if(Integer.parseInt(FragmentCargarMaterias.materias.get(k).getCreditos()) > 4){
                                                                        dia = v.findViewById(R.id.viernes18a19); dia.setText(clase);
                                                                    }
                                                                    break;
                                                                case "19:00":
                                                                    dia = v.findViewById(R.id.lunes19a20); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.martes19a20); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.miercoles19a20); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.jueves19a20); dia.setText(clase);
                                                                    if(Integer.parseInt(FragmentCargarMaterias.materias.get(k).getCreditos()) > 4){
                                                                        dia = v.findViewById(R.id.viernes19a20); dia.setText(clase);
                                                                    }
                                                                    break;
                                                                case "20:00":
                                                                    dia = v.findViewById(R.id.lunes20a21); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.martes20a21); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.miercoles20a21); dia.setText(clase);
                                                                    dia = v.findViewById(R.id.jueves20a21); dia.setText(clase);
                                                                    if(Integer.parseInt(FragmentCargarMaterias.materias.get(k).getCreditos()) > 4){
                                                                        dia = v.findViewById(R.id.viernes20a21); dia.setText(clase);
                                                                    }
                                                                    break;
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                            } else {//Si el horario ya fue asignado, se recuperan los datos de las materias
                                                guardar.setEnabled(false);
                                                FirebaseDatabase.getInstance().getReference().child("seleccionmaterias").orderByChild("estudiante").equalTo(FragmentCargarMaterias.numControlString)
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                    String[] elementos = snapshot.getKey().split("%");
                                                                    //Si es un grupo del periodo actual
                                                                    if(elementos[3].equals(FragmentCargarMaterias.periodoActual)){
                                                                        //Se recuperan la clave de la materia y el grupo
                                                                        claves.add(elementos[2]); claves.add(elementos[0]);
                                                                    }
                                                                }
                                                                //Se procede a recuperar los datos del grupo
                                                                FirebaseDatabase.getInstance().getReference().child("grupos").orderByChild("periodo").equalTo(FragmentCargarMaterias.periodoActual)
                                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                                    Grupo gpo = snapshot.getValue(Grupo.class);
                                                                                    for(int i=0; i<claves.size(); i+=2){
                                                                                        //Si es un gpo del estudiante
                                                                                        if((claves.get(i+1).equals(gpo.getNombre()))&&(claves.get(i).equals(gpo.getMateria()))){
                                                                                            materiasCargadas.add(gpo.getNombre());
                                                                                            materiasCargadas.add(gpo.getMateria());
                                                                                            materiasCargadas.add(gpo.getHora());
                                                                                            materiasCargadas.add("");
                                                                                            materiasCargadas.add(gpo.getAula());
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                //Ahora se cargan los datos de la materia
                                                                                FirebaseDatabase.getInstance().getReference().child("materias").orderByChild("plan").equalTo(FragmentCargarMaterias.plan)
                                                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                                                    Materia materia = snapshot.getValue(Materia.class);
                                                                                                    for(int i=0; i<materiasCargadas.size(); i+= 5){
                                                                                                        if(materiasCargadas.get(i+1).equals(snapshot.getKey())){
                                                                                                            materiasCargadas.set(i+1, materia.getNombreCorto());
                                                                                                            materiasCargadas.set(i+3, materia.getCreditos());
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                //FINALMENTE mostramos el horario
                                                                                                for(int i=0; i<materiasCargadas.size(); i+=5){
                                                                                                    String clase = materiasCargadas.get(i+1)+", \nAula: "+materiasCargadas.get(i+4);
                                                                                                    switch(materiasCargadas.get(i+2)){
                                                                                                        case "07:00":
                                                                                                            TextView dia = v.findViewById(R.id.lunes7a8); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.martes7a8); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.miercoles7a8); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.jueves7a8); dia.setText(clase);
                                                                                                            if(Integer.parseInt(materiasCargadas.get(i+3)) > 4){
                                                                                                                dia = v.findViewById(R.id.viernes7a8); dia.setText(clase);
                                                                                                            }
                                                                                                            break;
                                                                                                        case "08:00":
                                                                                                            dia = v.findViewById(R.id.lunes8a9); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.martes8a9); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.miercoles8a9); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.jueves8a9); dia.setText(clase);
                                                                                                            if(Integer.parseInt(materiasCargadas.get(i+3)) > 4){
                                                                                                                dia = v.findViewById(R.id.viernes8a9); dia.setText(clase);
                                                                                                            }
                                                                                                            break;
                                                                                                        case "09:00":
                                                                                                            dia = v.findViewById(R.id.lunes9a10); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.martes9a10); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.miercoles9a10); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.jueves9a10); dia.setText(clase);
                                                                                                            if(Integer.parseInt(materiasCargadas.get(i+3)) > 4){
                                                                                                                dia = v.findViewById(R.id.viernes9a10); dia.setText(clase);
                                                                                                            }
                                                                                                            break;
                                                                                                        case "10:00":
                                                                                                            dia = v.findViewById(R.id.lunes10a11); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.martes10a11); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.miercoles10a11); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.jueves10a11); dia.setText(clase);
                                                                                                            if(Integer.parseInt(materiasCargadas.get(i+3)) > 4){
                                                                                                                dia = v.findViewById(R.id.viernes10a11); dia.setText(clase);
                                                                                                            }
                                                                                                            break;
                                                                                                        case "11:00":
                                                                                                            dia = v.findViewById(R.id.lunes11a12); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.martes11a12); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.miercoles11a12); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.jueves11a12); dia.setText(clase);
                                                                                                            if(Integer.parseInt(materiasCargadas.get(i+3)) > 4){
                                                                                                                dia = v.findViewById(R.id.viernes11a12); dia.setText(clase);
                                                                                                            }
                                                                                                            break;
                                                                                                        case "12:00":
                                                                                                            dia = v.findViewById(R.id.lunes12a13); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.martes12a13); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.miercoles12a13); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.jueves12a13); dia.setText(clase);
                                                                                                            if(Integer.parseInt(materiasCargadas.get(i+3)) > 4){
                                                                                                                dia = v.findViewById(R.id.viernes12a13); dia.setText(clase);
                                                                                                            }
                                                                                                            break;
                                                                                                        case "13:00":
                                                                                                            dia = v.findViewById(R.id.lunes13a14); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.martes13a14); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.miercoles13a14); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.jueves13a14); dia.setText(clase);
                                                                                                            if(Integer.parseInt(materiasCargadas.get(i+3)) > 4){
                                                                                                                dia = v.findViewById(R.id.viernes13a14); dia.setText(clase);
                                                                                                            }
                                                                                                            break;
                                                                                                        case "14:00":
                                                                                                            dia = v.findViewById(R.id.lunes14a15); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.martes14a15); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.miercoles14a15); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.jueves14a15); dia.setText(clase);
                                                                                                            if(Integer.parseInt(materiasCargadas.get(i+3)) > 4){
                                                                                                                dia = v.findViewById(R.id.viernes14a15); dia.setText(clase);
                                                                                                            }
                                                                                                            break;
                                                                                                        case "15:00":
                                                                                                            dia = v.findViewById(R.id.lunes15a16); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.martes15a16); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.miercoles15a16); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.jueves15a16); dia.setText(clase);
                                                                                                            if(Integer.parseInt(materiasCargadas.get(i+3)) > 4){
                                                                                                                dia = v.findViewById(R.id.viernes15a16); dia.setText(clase);
                                                                                                            }
                                                                                                            break;
                                                                                                        case "16:00":
                                                                                                            dia = v.findViewById(R.id.lunes16a17); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.martes16a17); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.miercoles16a17); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.jueves16a17); dia.setText(clase);
                                                                                                            if(Integer.parseInt(materiasCargadas.get(i+3)) > 4){
                                                                                                                dia = v.findViewById(R.id.viernes16a17); dia.setText(clase);
                                                                                                            }
                                                                                                            break;
                                                                                                        case "17:00":
                                                                                                            dia = v.findViewById(R.id.lunes17a18); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.martes17a18); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.miercoles17a18); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.jueves17a18); dia.setText(clase);
                                                                                                            if(Integer.parseInt(materiasCargadas.get(i+3)) > 4){
                                                                                                                dia = v.findViewById(R.id.viernes17a18); dia.setText(clase);
                                                                                                            }
                                                                                                            break;
                                                                                                        case "18:00":
                                                                                                            dia = v.findViewById(R.id.lunes18a19); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.martes18a19); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.miercoles18a19); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.jueves18a19); dia.setText(clase);
                                                                                                            if(Integer.parseInt(materiasCargadas.get(i+3)) > 4){
                                                                                                                dia = v.findViewById(R.id.viernes18a19); dia.setText(clase);
                                                                                                            }
                                                                                                            break;
                                                                                                        case "19:00":
                                                                                                            dia = v.findViewById(R.id.lunes19a20); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.martes19a20); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.miercoles19a20); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.jueves19a20); dia.setText(clase);
                                                                                                            if(Integer.parseInt(materiasCargadas.get(i+3)) > 4){
                                                                                                                dia = v.findViewById(R.id.viernes19a20); dia.setText(clase);
                                                                                                            }
                                                                                                            break;
                                                                                                        case "20:00":
                                                                                                            dia = v.findViewById(R.id.lunes20a21); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.martes20a21); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.miercoles20a21); dia.setText(clase);
                                                                                                            dia = v.findViewById(R.id.jueves20a21); dia.setText(clase);
                                                                                                            if(Integer.parseInt(materiasCargadas.get(i+3)) > 4){
                                                                                                                dia = v.findViewById(R.id.viernes20a21); dia.setText(clase);
                                                                                                            }
                                                                                                            break;
                                                                                                    }
                                                                                                }
                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                            }
                                                                                        });
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        } else {
                            Toast.makeText(activity, "No existe este número de control", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        }
    }
    public static void limpiarCampos(){
        //LUNES
        TextView lunes = v.findViewById(R.id.lunes7a8); lunes.setText("");
        lunes = v.findViewById(R.id.lunes8a9); lunes.setText("");
        lunes = v.findViewById(R.id.lunes9a10); lunes.setText("");
        lunes = v.findViewById(R.id.lunes10a11); lunes.setText("");
        lunes = v.findViewById(R.id.lunes11a12); lunes.setText("");
        lunes = v.findViewById(R.id.lunes12a13); lunes.setText("");
        lunes = v.findViewById(R.id.lunes13a14); lunes.setText("");
        lunes = v.findViewById(R.id.lunes14a15); lunes.setText("");
        lunes = v.findViewById(R.id.lunes15a16); lunes.setText("");
        lunes = v.findViewById(R.id.lunes16a17); lunes.setText("");
        lunes = v.findViewById(R.id.lunes17a18); lunes.setText("");
        lunes = v.findViewById(R.id.lunes18a19); lunes.setText("");
        lunes = v.findViewById(R.id.lunes19a20); lunes.setText("");
        lunes = v.findViewById(R.id.lunes20a21); lunes.setText("");
        //MARTES
        lunes = v.findViewById(R.id.martes7a8); lunes.setText("");
        lunes = v.findViewById(R.id.martes8a9); lunes.setText("");
        lunes = v.findViewById(R.id.martes9a10); lunes.setText("");
        lunes = v.findViewById(R.id.martes10a11); lunes.setText("");
        lunes = v.findViewById(R.id.martes11a12); lunes.setText("");
        lunes = v.findViewById(R.id.martes12a13); lunes.setText("");
        lunes = v.findViewById(R.id.martes13a14); lunes.setText("");
        lunes = v.findViewById(R.id.martes14a15); lunes.setText("");
        lunes = v.findViewById(R.id.martes15a16); lunes.setText("");
        lunes = v.findViewById(R.id.martes16a17); lunes.setText("");
        lunes = v.findViewById(R.id.martes17a18); lunes.setText("");
        lunes = v.findViewById(R.id.martes18a19); lunes.setText("");
        lunes = v.findViewById(R.id.martes19a20); lunes.setText("");
        lunes = v.findViewById(R.id.martes20a21); lunes.setText("");
        //MIERCOLES
        lunes = v.findViewById(R.id.miercoles7a8); lunes.setText("");
        lunes = v.findViewById(R.id.miercoles8a9); lunes.setText("");
        lunes = v.findViewById(R.id.miercoles9a10); lunes.setText("");
        lunes = v.findViewById(R.id.miercoles10a11); lunes.setText("");
        lunes = v.findViewById(R.id.miercoles11a12); lunes.setText("");
        lunes = v.findViewById(R.id.miercoles12a13); lunes.setText("");
        lunes = v.findViewById(R.id.miercoles13a14); lunes.setText("");
        lunes = v.findViewById(R.id.miercoles14a15); lunes.setText("");
        lunes = v.findViewById(R.id.miercoles15a16); lunes.setText("");
        lunes = v.findViewById(R.id.miercoles16a17); lunes.setText("");
        lunes = v.findViewById(R.id.miercoles17a18); lunes.setText("");
        lunes = v.findViewById(R.id.miercoles18a19); lunes.setText("");
        lunes = v.findViewById(R.id.miercoles19a20); lunes.setText("");
        lunes = v.findViewById(R.id.miercoles20a21); lunes.setText("");
        //JUEVES
        lunes = v.findViewById(R.id.jueves7a8); lunes.setText("");
        lunes = v.findViewById(R.id.jueves8a9); lunes.setText("");
        lunes = v.findViewById(R.id.jueves9a10); lunes.setText("");
        lunes = v.findViewById(R.id.jueves10a11); lunes.setText("");
        lunes = v.findViewById(R.id.jueves11a12); lunes.setText("");
        lunes = v.findViewById(R.id.jueves12a13); lunes.setText("");
        lunes = v.findViewById(R.id.jueves13a14); lunes.setText("");
        lunes = v.findViewById(R.id.jueves14a15); lunes.setText("");
        lunes = v.findViewById(R.id.jueves15a16); lunes.setText("");
        lunes = v.findViewById(R.id.jueves16a17); lunes.setText("");
        lunes = v.findViewById(R.id.jueves17a18); lunes.setText("");
        lunes = v.findViewById(R.id.jueves18a19); lunes.setText("");
        lunes = v.findViewById(R.id.jueves19a20); lunes.setText("");
        lunes = v.findViewById(R.id.jueves20a21); lunes.setText("");
        //VIERNES
        lunes = v.findViewById(R.id.viernes7a8); lunes.setText("");
        lunes = v.findViewById(R.id.viernes8a9); lunes.setText("");
        lunes = v.findViewById(R.id.viernes9a10); lunes.setText("");
        lunes = v.findViewById(R.id.viernes10a11); lunes.setText("");
        lunes = v.findViewById(R.id.viernes11a12); lunes.setText("");
        lunes = v.findViewById(R.id.viernes12a13); lunes.setText("");
        lunes = v.findViewById(R.id.viernes13a14); lunes.setText("");
        lunes = v.findViewById(R.id.viernes14a15); lunes.setText("");
        lunes = v.findViewById(R.id.viernes15a16); lunes.setText("");
        lunes = v.findViewById(R.id.viernes16a17); lunes.setText("");
        lunes = v.findViewById(R.id.viernes17a18); lunes.setText("");
        lunes = v.findViewById(R.id.viernes18a19); lunes.setText("");
        lunes = v.findViewById(R.id.viernes19a20); lunes.setText("");
        lunes = v.findViewById(R.id.viernes20a21); lunes.setText("");
        //SABADO
        lunes = v.findViewById(R.id.sabado7a8); lunes.setText("");
        lunes = v.findViewById(R.id.sabado8a9); lunes.setText("");
        lunes = v.findViewById(R.id.sabado9a10); lunes.setText("");
        lunes = v.findViewById(R.id.sabado10a11); lunes.setText("");
        lunes = v.findViewById(R.id.sabado11a12); lunes.setText("");
        lunes = v.findViewById(R.id.sabado12a13); lunes.setText("");
        lunes = v.findViewById(R.id.sabado13a14); lunes.setText("");
        lunes = v.findViewById(R.id.sabado14a15); lunes.setText("");
        lunes = v.findViewById(R.id.sabado15a16); lunes.setText("");
        lunes = v.findViewById(R.id.sabado16a17); lunes.setText("");
        lunes = v.findViewById(R.id.sabado17a18); lunes.setText("");
        lunes = v.findViewById(R.id.sabado18a19); lunes.setText("");
        lunes = v.findViewById(R.id.sabado19a20); lunes.setText("");
        lunes = v.findViewById(R.id.sabado20a21); lunes.setText("");
    }

    //Acción del botón
    View.OnClickListener guardarHorario = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseDatabase.getInstance().getReference().child("materias").orderByChild("plan").equalTo(FragmentCargarMaterias.plan)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Materia m = snapshot.getValue(Materia.class);
                                for(int i=0; i<FragmentCargarMaterias.materias.size(); i++){
                                    if(m.getClave().equals(FragmentCargarMaterias.materias.get(i).getClave())) {
                                        for (int k = 0; k < claves.size(); k += 2) {
                                            if (claves.get(k).equals(FragmentCargarMaterias.materias.get(i).getClave())) {
                                                String clave = FragmentCargarMaterias.materias.get(i).getGrupo() + "%" +
                                                        FragmentCargarMaterias.plan + "%" +
                                                        snapshot.getKey() + "%" + FragmentCargarMaterias.periodoActual + "%" +
                                                        FragmentCargarMaterias.numControlString;
                                                //Cada materia seleccionada se agrega a la BD
                                                FirebaseDatabase.getInstance().getReference().child("seleccionmaterias").child(clave).setValue(new SeleccionMaterias(FragmentCargarMaterias.numControlString));
                                            }
                                        }
                                    }
                                }
                            }
                            FragmentCargarMaterias.materias.clear();
                            FragmentCargarMaterias.rvMaterias.getAdapter().notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }
    };
}
