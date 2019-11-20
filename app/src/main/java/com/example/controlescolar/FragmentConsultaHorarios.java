package com.example.controlescolar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentConsultaHorarios extends Fragment {
    Activity activity;
    public static View v;
    static EditText numControl;
    static Button btnBuscar;
    Spinner spinnerPeriodo;
    static String periodo, plan;
    static ArrayAdapter adapterPeriodos;
    static ArrayList<String> array;
    public static ArrayList<String> claves = new ArrayList<String>();
    public static ArrayList<String> materiasCargadas = new ArrayList<String>();

    public FragmentConsultaHorarios(Activity activity){
        this.activity = activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_fragment_consulta_horarios, container, false);
        btnBuscar = v.findViewById(R.id.btnConsulta);
        btnBuscar.setOnClickListener(consultar);
        numControl = v.findViewById(R.id.numControl);
        spinnerPeriodo = v.findViewById(R.id.spinnerPeriodo);
        cargarPeriodos();
        return v;
    }

    public void cargarPeriodos(){
        array = new ArrayList<String>();
        adapterPeriodos = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, array);
        spinnerPeriodo.setAdapter(adapterPeriodos);
        int anio = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=anio-6; i<=anio; i++){
            array.add("FEB-JUN/"+String.valueOf(i));
            array.add("VERANO/"+String.valueOf(i));
            array.add("AGO-DIC/"+String.valueOf(i));
        }
        adapterPeriodos.notifyDataSetChanged();
    }
    View.OnClickListener consultar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Si no hay campos vacios
            if (!numControl.getText().toString().equals("")) {
                //Se obtiene el periodo
                String[] elementos = spinnerPeriodo.getSelectedItem().toString().split("/");
                if (elementos[0].equals("FEB-JUN")) {
                    periodo = elementos[1] + "1";
                } else if (elementos[0].equals("VERANO")) {
                    periodo = elementos[1] + "2";
                } else {
                    periodo = elementos[1] + "3";
                }
                FirebaseDatabase.getInstance().getReference().child("seleccionmaterias").orderByChild("estudiante").equalTo(numControl.getText().toString())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String[] elementos = snapshot.getKey().split("%");
                                    plan = elementos[1];
                                    //Si es un grupo del periodo actual
                                    if (elementos[3].equals(FragmentCargarMaterias.periodoActual)) {
                                        //Se recuperan la clave de la materia y el grupo
                                        claves.add(elementos[2]);
                                        claves.add(elementos[0]);
                                    }
                                }
                                //Se procede a recuperar los datos del grupo
                                FirebaseDatabase.getInstance().getReference().child("grupos").orderByChild("periodo").equalTo(periodo)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()) {
                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                        Grupo gpo = snapshot.getValue(Grupo.class);
                                                        for (int i = 0; i < claves.size(); i += 2) {
                                                            //Si es un gpo del estudiante
                                                            if ((claves.get(i + 1).equals(gpo.getNombre())) && (claves.get(i).equals(gpo.getMateria()))) {
                                                                materiasCargadas.add(gpo.getNombre());
                                                                materiasCargadas.add(gpo.getMateria());
                                                                materiasCargadas.add(gpo.getHora());
                                                                materiasCargadas.add("");
                                                                materiasCargadas.add(gpo.getAula());
                                                                materiasCargadas.add("");
                                                                materiasCargadas.add(gpo.getDocente());
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    //Ahora se cargan los datos de la materia
                                                    FirebaseDatabase.getInstance().getReference().child("materias").orderByChild("plan").equalTo(plan)
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                        Materia materia = snapshot.getValue(Materia.class);
                                                                        for (int i = 0; i < materiasCargadas.size(); i += 7) {
                                                                            if (materiasCargadas.get(i + 1).equals(snapshot.getKey())) {
                                                                                materiasCargadas.set(i + 1, materia.getNombreCorto());
                                                                                materiasCargadas.set(i + 3, materia.getCreditos());
                                                                                materiasCargadas.set(i + 5, FragmentCargarMaterias.coloresFondoH.get(0));
                                                                                FragmentCargarMaterias.coloresFondoH.remove(0);
                                                                            }
                                                                        }
                                                                    }
                                                                    TextView tvNombresMat = v.findViewById(R.id.materiasNom);
                                                                    TextView tvDocentes = v.findViewById(R.id.doc);
                                                                    String misMaterias = "", misDocentes = "";
                                                                    //FINALMENTE mostramos el horario
                                                                    for (int i = 0; i < materiasCargadas.size(); i += 7) {
                                                                        misMaterias += materiasCargadas.get(i+1)+"\n";
                                                                        if(materiasCargadas.get(i+6) != null){
                                                                            String[] elementos = materiasCargadas.get(i+6).split(" ");
                                                                            misDocentes += elementos[0]+" "+elementos[elementos.length-1]+"\n";
                                                                        }else{
                                                                            misDocentes += "SIN ASIGNAR\n";
                                                                        }
                                                                        String clase = materiasCargadas.get(i + 1) + ", \nAula: " + materiasCargadas.get(i + 4);
                                                                        switch (materiasCargadas.get(i + 2)) {
                                                                            case "07:00-08:00":
                                                                                TextView dia = v.findViewById(R.id.lunes7a8);
                                                                                dia.setText(clase); dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes7a8);
                                                                                dia.setText(clase); dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles7a8);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves7a8);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes7a8);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "08:00-09:00":
                                                                                dia = v.findViewById(R.id.lunes8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes8a9);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "09:00-10:00":
                                                                                dia = v.findViewById(R.id.lunes9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes9a10);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "10:00-11:00":
                                                                                dia = v.findViewById(R.id.lunes10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes10a11);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "11:00-12:00":
                                                                                dia = v.findViewById(R.id.lunes11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes11a12);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "12:00-13:00":
                                                                                dia = v.findViewById(R.id.lunes12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes12a13);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "13:00-14:00":
                                                                                dia = v.findViewById(R.id.lunes13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes13a14);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "14:00-15:00":
                                                                                dia = v.findViewById(R.id.lunes14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes14a15);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "15:00-16:00":
                                                                                dia = v.findViewById(R.id.lunes15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes15a16);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "16:00-17:00":
                                                                                dia = v.findViewById(R.id.lunes16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes16a17);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "17:00-18:00":
                                                                                dia = v.findViewById(R.id.lunes17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes17a18);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "18:00-19:00":
                                                                                dia = v.findViewById(R.id.lunes18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes18a19);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "19:00-20:00":
                                                                                dia = v.findViewById(R.id.lunes19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes19a20);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "20:00-21:00":
                                                                                dia = v.findViewById(R.id.lunes20a21);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes20a21);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles20a21);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves20a21);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes20a21);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "07:00-09:00":
                                                                                dia = v.findViewById(R.id.lunes7a8);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes7a8);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles7a8);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves7a8);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes7a8);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes8a9);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "08:00-10:00":
                                                                                dia = v.findViewById(R.id.lunes8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes8a9);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes9a10);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "09:00:11:00":
                                                                                dia = v.findViewById(R.id.lunes9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes9a10);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes10a11);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "10:00-12:00":
                                                                                dia = v.findViewById(R.id.lunes10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes10a11);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes11a12);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "11:00-13:00":
                                                                                dia = v.findViewById(R.id.lunes11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes11a12);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes12a13);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "12:00-14:00":
                                                                                dia = v.findViewById(R.id.lunes12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes12a13);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes13a14);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "13:00-15:00":
                                                                                dia = v.findViewById(R.id.lunes13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes13a14);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes14a15);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "14:00-16:00":
                                                                                dia = v.findViewById(R.id.lunes14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes14a15);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes15a16);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "15:00-17:00":
                                                                                dia = v.findViewById(R.id.lunes15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes15a16);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes16a17);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "16:00-18:00":
                                                                                dia = v.findViewById(R.id.lunes16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes16a17);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes17a18);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "17:00-19:00":
                                                                                dia = v.findViewById(R.id.lunes17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes17a18);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes18a19);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "18:00-20:00":
                                                                                dia = v.findViewById(R.id.lunes18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes18a19);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes19a20);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "19:00-21:00":
                                                                                dia = v.findViewById(R.id.lunes19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes19a20);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes20a21);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes20a21);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles20a21);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves20a21);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes20a21);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "07:00:10:00":
                                                                                dia = v.findViewById(R.id.lunes7a8);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes7a8);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles7a8);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves7a8);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes7a8);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes8a9);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes9a10);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "08:00-11:00":
                                                                                dia = v.findViewById(R.id.lunes8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves8a9);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes8a9);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes9a10);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes10a11);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "09:00:12:00":
                                                                                dia = v.findViewById(R.id.lunes9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves9a10);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes9a10);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes10a11);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes11a12);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "10:00-13:00":
                                                                                dia = v.findViewById(R.id.lunes10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves10a11);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes10a11);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes11a12);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes12a13);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "11:00-14:00":
                                                                                dia = v.findViewById(R.id.lunes11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves11a12);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes11a12);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes12a13);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes13a14);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "12:00-15:00":
                                                                                dia = v.findViewById(R.id.lunes12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves12a13);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes12a13);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes13a14);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes14a15);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "13:00-16:00":
                                                                                dia = v.findViewById(R.id.lunes13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves13a14);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes13a14);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes14a15);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes15a16);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "14:00-17:00":
                                                                                dia = v.findViewById(R.id.lunes14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves14a15);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes14a15);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes15a16);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes16a17);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "15:00-18:00":
                                                                                dia = v.findViewById(R.id.lunes15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves15a16);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes15a16);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes16a17);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes17a18);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "16:00-19:00":
                                                                                dia = v.findViewById(R.id.lunes16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves16a17);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes16a17);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes17a18);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes18a19);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "17:00-20:00":
                                                                                dia = v.findViewById(R.id.lunes17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves17a18);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes17a18);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes18a19);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes19a20);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                            case "18:00-21:00":
                                                                                dia = v.findViewById(R.id.lunes18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves18a19);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes18a19);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves19a20);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes19a20);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                dia = v.findViewById(R.id.lunes20a21);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.martes20a21);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.miercoles20a21);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                dia = v.findViewById(R.id.jueves20a21);
                                                                                dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                if (Integer.parseInt(materiasCargadas.get(i + 3)) > 4) {
                                                                                    dia = v.findViewById(R.id.viernes20a21);
                                                                                    dia.setText(clase);dia.setTextColor(Color.parseColor(materiasCargadas.get(i + 5)));
                                                                                }
                                                                                break;
                                                                        }
                                                                        tvNombresMat.setText(misMaterias);
                                                                        tvDocentes.setText(misDocentes);
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                } else {
                                                    limpiarCampos();
                                                    Toast.makeText(activity, "No hay registros de materia en este periodo", Toast.LENGTH_SHORT).show();
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


            } else {
                Toast.makeText(activity, "No existe este número de control", Toast.LENGTH_SHORT).show();
            }
        }
    };

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

};
