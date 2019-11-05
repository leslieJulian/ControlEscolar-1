package com.example.controlescolar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class FragmentCargarMaterias extends Fragment {
    View v; Activity actividad;
    static RecyclerView rvMaterias;
    SwipeRefreshLayout srCargarMaterias;
    public static ArrayList<PojoMateria> materias;
    ArrayList<String> listaMaterias;
    public static ArrayList<String> coloresFondo;
    ArrayList<String> evaluacionesAlumno;
    String periodoRegistro, especialidad, semestre = "";
    public static String periodoActual, numControlString = "", plan;
    EditText numControl;

    public FragmentCargarMaterias(){
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_cargar_materias, container, false);
        actividad = this.getActivity();

        //Obtenidneo los datos necesarios
        getPeriodoActual();
        rvMaterias = v.findViewById(R.id.rvMaterias);
        LinearLayoutManager lim = new LinearLayoutManager(getActivity());
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        rvMaterias.setLayoutManager(lim);
        materias = new ArrayList<PojoMateria>();

        Button btnCargar = v.findViewById(R.id.btnCargarMaterias);
        btnCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inicializarDatos();
                MateriasAdaptador adaptador = new MateriasAdaptador(materias, actividad);
                rvMaterias.setAdapter(adaptador);
            }
        });

        srCargarMaterias = v.findViewById(R.id.refreshMaterias);
        srCargarMaterias.setOnRefreshListener(refreshAction);

        return v;
    }

    //Métodos de recuperacion de datos
    public void getPeriodoActual(){
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
                                periodoActual = tecnologico.getPeriodoactual();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Toast.makeText(getActivity(), databaseError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void inicializarDatos(){
        materias.clear();
        rellenarColores();
        FragmentHorario.claves.clear();
        FragmentHorario.limpiarCampos();
        numControl = v.findViewById(R.id.numeroControl);
        numControlString = numControl.getText().toString();
        //Validamos num de control
        if(!numControl.getText().toString().equals("")){
            //Verificamos que el alumno existe
            FirebaseDatabase.getInstance().getReference().child("estudiantes").child(numControl.getText().toString())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                //Guardamos su periodo de registro
                                Estudiante estudiante = dataSnapshot.getValue(Estudiante.class);
                                periodoRegistro = estudiante.getPeriodo();
                                plan = estudiante.getPlan();
                                especialidad = estudiante.getEspecialidad();
                                //Verificamos que el estudiante aun no tenga materias cargadas para este periodo
                                FirebaseDatabase.getInstance().getReference().child("seleccionmaterias").orderByChild("estudiante").equalTo(numControl.getText().toString())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                boolean valor = true;
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    String[] elementos = snapshot.getKey().split("%");
                                                    //Si existen materias seleccionadas en el periodo actual por el estudiante
                                                    //No se permiten cargar nuevamente
                                                    if(elementos[3].equals(periodoActual)){
                                                        valor = false;
                                                        break;
                                                    }
                                                }
                                                //Si aun no ha cargado materias el estudiante, se procede a validar las materias que podria
                                                //cursar
                                                if(valor){
                                                    //Obteniendo su semestre
                                                    if(periodoActual.equals(periodoRegistro)){
                                                        semestre = "1";
                                                        //Listamos todas las materias para 1er semestre
                                                        FirebaseDatabase.getInstance().getReference().child("materias").orderByChild("plan").equalTo(plan)
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.exists()) {
                                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                                //Si es materia de 1er semestre se agrega a la lista
                                                                                Materia materia = snapshot.getValue(Materia.class);
                                                                                if (materia.getSemestre().equals("1")) {
                                                                                    final String claveMateria = materia.getClave();
                                                                                    final String nombreMateria = materia.getNombre();
                                                                                    final String semestreMateria = materia.getSemestre();
                                                                                    final String creditosMateria = materia.getCreditos();
                                                                                    //Obteniendo los grupos para ver el horario de la materia
                                                                                    FirebaseDatabase.getInstance().getReference().child("grupos").orderByChild("materia")
                                                                                            .equalTo(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                                                String[] elementos = snapshot.getKey().split("%");
                                                                                                //Si corresponde al periodo actual
                                                                                                if (elementos[3].equals(periodoActual)) {
                                                                                                    Grupo grupo = snapshot.getValue(Grupo.class);
                                                                                                    materias.add(new PojoMateria(claveMateria, nombreMateria, semestreMateria, creditosMateria, grupo.getHora(), grupo.getAula(), grupo.getNombre(), "#ffffff"));
                                                                                                }
                                                                                            }
                                                                                            rvMaterias.getAdapter().notifyDataSetChanged();
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });
                                                    }else {
                                                        //Se separan los años y periodos para calcular el semestre del estudiante
                                                        int anioRegistro = Integer.parseInt(periodoRegistro.substring(0, periodoRegistro.length() - 1));
                                                        int anioActual = Integer.parseInt(periodoActual.substring(0, periodoActual.length() - 1));
                                                        String perRegistro = periodoRegistro.substring(4,5);
                                                        String perActual = periodoActual.substring(4,5);
                                                        //Si es periodo de verano
                                                        if(perActual.equals("2")){
                                                            //Si ingresó en AGO-DIC (3)
                                                            if(perRegistro.equals("3")){
                                                                semestre = String.valueOf((anioActual-anioRegistro)*2);
                                                            }else{//Si ingresó en FEB-JUN (1)
                                                                //Si es el mismo año
                                                                if(anioRegistro == anioActual){
                                                                    semestre = "1";
                                                                }else{
                                                                    semestre = String.valueOf(((anioActual-anioRegistro)*2)+1);
                                                                }
                                                            }
                                                        }
                                                        //Si es el mismo periodo (para 1 o 3)
                                                        else {
                                                            if(perRegistro.equals(perActual)){
                                                                semestre = String.valueOf(((anioActual-anioRegistro)*2)+1);
                                                                //Si ingresó en AGO-DIC y el periodo actual es FEB-JUN
                                                            }else if(perRegistro.equals("3") && perActual.equals("1")){
                                                                semestre = String.valueOf((anioActual-anioRegistro)*2);
                                                                //Si ingresó en FEB-JUN y es AGO-DIC
                                                            }else{
                                                                semestre = String.valueOf(((anioActual-anioRegistro)*2)+2);
                                                            }
                                                        }
                                                        cargarMaterias();
                                                    }
                                                }else{
                                                    FragmentHorario.cargarHorario();
                                                    Toast.makeText(getActivity(), "El estudiante ya tiene asignadas sus materias", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                            }else{
                                numControl.setText("");
                                Toast.makeText(getActivity(), "No existe este número de control", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }else{
            // No se ingresó un numero de control
            Toast.makeText(getActivity(), "Ingrese un número de control válido", Toast.LENGTH_SHORT).show();
        }
    }

    SwipeRefreshLayout.OnRefreshListener refreshAction = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            inicializarDatos();
            srCargarMaterias.setRefreshing(false);
        }
    };

    public void cargarMaterias(){
        //Si no tiene materias aisgnadas
        if(!semestre.equals("Asignado")) {
            listaMaterias = new ArrayList<String>();
            evaluacionesAlumno = new ArrayList<String>();
            //Listamos todas las materias del semestre actual
            FirebaseDatabase.getInstance().getReference().child("materias").orderByChild("plan").equalTo(plan)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                //Obtenemos la materia
                                Materia materia = snapshot.getValue(Materia.class);
                                //Verificamos que la materia sea menor o igual al semestre actual
                                if(Integer.parseInt(materia.getSemestre()) <= Integer.parseInt(semestre)){
                                    //Si la materia es de especialidad, verificamos que coincida con la esp. del estudiante
                                    boolean es_especialidad = false;
                                    if(materia.getEspecialidad() != null){
                                        if(materia.getEspecialidad().equals(especialidad)){
                                            es_especialidad = true;
                                        }
                                    }else{
                                        es_especialidad = true;
                                    }
                                    if(es_especialidad){
                                        listaMaterias.add(snapshot.getKey());
                                        listaMaterias.add(materia.getClave());
                                        listaMaterias.add(materia.getNombre());
                                        listaMaterias.add(materia.getSemestre());
                                        listaMaterias.add(materia.getCreditos());
                                        listaMaterias.add(materia.getRequerimiento1());
                                        listaMaterias.add(materia.getRequerimiento2());
                                        listaMaterias.add(materia.getRequerimiento3());
                                        listaMaterias.add(materia.getRequerimiento4());
                                        listaMaterias.add(materia.getRequerimiento5());
                                    }
                                }
                            }
                            //Listamos todas las materias que ya ha aprobado el alumno
                            FirebaseDatabase.getInstance().getReference().child("estudiantes").child(numControl.getText().toString()).child("evaluaciones")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                Evaluacion eva = snapshot.getValue(Evaluacion.class);
                                                //Si es aprobatoria se añade
                                                if(Integer.parseInt(eva.getCalificacion()) >= 70){
                                                    evaluacionesAlumno.add(snapshot.getKey());
                                                }
                                            }
                                            //Si ya aprobó una materia, se elimina ésta del arreglo de listaMaterias
                                            for(int i=0; i<evaluacionesAlumno.size(); i++){
                                                for(int k=0; k<listaMaterias.size(); k+=10){
                                                    if(listaMaterias.get(k).equals(evaluacionesAlumno.get(i))){
                                                        listaMaterias.remove(k); listaMaterias.remove(k); listaMaterias.remove(k); listaMaterias.remove(k); listaMaterias.remove(k);
                                                        listaMaterias.remove(k); listaMaterias.remove(k); listaMaterias.remove(k); listaMaterias.remove(k); listaMaterias.remove(k);
                                                        break;
                                                    }
                                                }
                                            }
                                            //Si tiene materias con requisitos que NO están aprobados, se eliminan
                                            for(int k=0; k<listaMaterias.size(); k+=10){
                                                for(int x=5; x<10; x++) {
                                                    Log.d("", ""+(k+x));
                                                    if (listaMaterias.get(k+x) != null) {
                                                        //Si tiene un requisito, se busca que se haya aprobado
                                                        boolean aprobada = false;
                                                        for (int i = 0; i < evaluacionesAlumno.size(); i++) {
                                                            //Si esta en la lista fue aprobada
                                                            if (listaMaterias.get(k + x).equals(evaluacionesAlumno.get(i))) {
                                                                aprobada = true;
                                                                break;
                                                            }
                                                        }
                                                        //Si no la ha pasado, se elimina
                                                        if (!aprobada) {
                                                            listaMaterias.remove(k); listaMaterias.remove(k); listaMaterias.remove(k); listaMaterias.remove(k);
                                                            listaMaterias.remove(k); listaMaterias.remove(k); listaMaterias.remove(k);
                                                            listaMaterias.remove(k); listaMaterias.remove(k); listaMaterias.remove(k);
                                                        }
                                                    }
                                                }
                                            }
                                            //Se buscan los grupos y se muestran los que puede cursar
                                            FirebaseDatabase.getInstance().getReference().child("grupos").orderByChild("periodo")
                                                    .equalTo(periodoActual).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                        Grupo grupo = snapshot.getValue(Grupo.class);
                                                        //Recorremos cada materia para enlistarla
                                                        for(int i=0; i<listaMaterias.size(); i+=10){
                                                            if(listaMaterias.get(i).equals(grupo.getMateria())) {
                                                                materias.add(new PojoMateria(listaMaterias.get(i+1), listaMaterias.get(i + 2), listaMaterias.get(i + 3), listaMaterias.get(i + 4), grupo.getHora(), grupo.getAula(), grupo.getNombre(), "#ffffff"));
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    rvMaterias.getAdapter().notifyDataSetChanged();
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

        }else{
            //Asignamos colores distintivos a cada card
        }
    }

    public void rellenarColores(){
        coloresFondo = new ArrayList<String>();
        coloresFondo.add("#ffadad"); coloresFondo.add("#c6ffad"); coloresFondo.add("#d7adff");
        coloresFondo.add("#ffdcad"); coloresFondo.add("#adffd7"); coloresFondo.add("ffadfb");
        coloresFondo.add("#feffad"); coloresFondo.add("#adc8ff"); coloresFondo.add("#ffadb8");
        coloresFondo.add("#c5c5c5");
    }
}
