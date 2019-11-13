package com.example.controlescolar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VisualizaMaterias extends AppCompatActivity {

    private adapterMateria mAdapter1, mAdapter2, mAdapter3, mAdapter4, mAdapter5, mAdapter6, mAdapter7, mAdapter8, mAdapter9;
    private RecyclerView rvMateriasS1, rvMateriasS2, rvMateriasS3, rvMateriasS4, rvMateriasS5, rvMateriasS6, rvMateriasS7, rvMateriasS8, rvMateriasS9;
    private TextView textSemestre1, textSemestre2, textSemestre3, textSemestre4, textSemestre5, textSemestre6, textSemestre7, textSemestre8, textSemestre9;
    private ArrayList<Materia> listaMateriasS1 = new ArrayList<>(),
            listaMateriasS2 = new ArrayList<>(),
            listaMateriasS3 = new ArrayList<>(),
            listaMateriasS4 = new ArrayList<>(),
            listaMateriasS5 = new ArrayList<>(),
            listaMateriasS6 = new ArrayList<>(),
            listaMateriasS7 = new ArrayList<>(),
            listaMateriasS8 = new ArrayList<>(),
            listaMateriasS9 = new ArrayList<>();

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance ();
    private DatabaseReference mDatabaseReference = mDatabase.getReference ().child("materias");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza_materias);

        agregarMateria();
        inicializarElementos();
        caragarDatos();
    }

    private void agregarMateria(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisualizaMaterias.this, AgregarMateria.class);
                VisualizaMaterias.this.startActivity(intent);
                VisualizaMaterias.this.finish();
                //Snackbar.make(view, "Agregar Materia", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    private void obtenerMaterias(final RecyclerView recyclerView, final int semestre, final TextView textView, final ArrayList<Materia> listMaterias){
        Query query = mDatabaseReference.orderByChild("semestre").equalTo(String.valueOf(semestre));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    listMaterias.clear();
                    for (DataSnapshot ds:dataSnapshot.getChildren()) {
                        Materia materia = ds.getValue(Materia.class);
                        listMaterias.add(materia);
                    }
                    clasificarMaterias(semestre, listMaterias, recyclerView);
                }else{
                    textView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inicializarElementos(){
        textSemestre1 = (TextView) findViewById(R.id.txtSemestre1);
        textSemestre2 = (TextView) findViewById(R.id.txtSemestre2);
        textSemestre3 = (TextView) findViewById(R.id.txtSemestre3);
        textSemestre4 = (TextView) findViewById(R.id.txtSemestre4);
        textSemestre5 = (TextView) findViewById(R.id.txtSemestre5);
        textSemestre6 = (TextView) findViewById(R.id.txtSemestre6);
        textSemestre7 = (TextView) findViewById(R.id.txtSemestre7);
        textSemestre8 = (TextView) findViewById(R.id.txtSemestre8);
        textSemestre9 = (TextView) findViewById(R.id.txtSemestre9);
        rvMateriasS1 = (RecyclerView) findViewById(R.id.recycler_view_Materias_Semestre1);
        rvMateriasS1.setLayoutManager(new LinearLayoutManager(this));
        rvMateriasS2 = (RecyclerView) findViewById(R.id.recycler_view_Materias_Semestre2);
        rvMateriasS2.setLayoutManager(new LinearLayoutManager(this));
        rvMateriasS3 = (RecyclerView) findViewById(R.id.recycler_view_Materias_Semestre3);
        rvMateriasS3.setLayoutManager(new LinearLayoutManager(this));
        rvMateriasS4 = (RecyclerView) findViewById(R.id.recycler_view_Materias_Semestre4);
        rvMateriasS4.setLayoutManager(new LinearLayoutManager(this));
        rvMateriasS5 = (RecyclerView) findViewById(R.id.recycler_view_Materias_Semestre5);
        rvMateriasS5.setLayoutManager(new LinearLayoutManager(this));
        rvMateriasS6 = (RecyclerView) findViewById(R.id.recycler_view_Materias_Semestre6);
        rvMateriasS6.setLayoutManager(new LinearLayoutManager(this));
        rvMateriasS7 = (RecyclerView) findViewById(R.id.recycler_view_Materias_Semestre7);
        rvMateriasS7.setLayoutManager(new LinearLayoutManager(this));
        rvMateriasS8 = (RecyclerView) findViewById(R.id.recycler_view_Materias_Semestre8);
        rvMateriasS8.setLayoutManager(new LinearLayoutManager(this));
        rvMateriasS9 = (RecyclerView) findViewById(R.id.recycler_view_Materias_Semestre9);
        rvMateriasS9.setLayoutManager(new LinearLayoutManager(this));
    }

    private void caragarDatos(){
        obtenerMaterias(rvMateriasS1, 1, textSemestre1, listaMateriasS1);
        obtenerMaterias(rvMateriasS2, 2, textSemestre2, listaMateriasS2);
        obtenerMaterias(rvMateriasS3, 3, textSemestre3, listaMateriasS3);
        obtenerMaterias(rvMateriasS4, 4, textSemestre4, listaMateriasS4);
        obtenerMaterias(rvMateriasS5, 5, textSemestre5, listaMateriasS5);
        obtenerMaterias(rvMateriasS6, 6, textSemestre6, listaMateriasS6);
        obtenerMaterias(rvMateriasS7, 7, textSemestre7, listaMateriasS7);
        obtenerMaterias(rvMateriasS8, 8, textSemestre8, listaMateriasS8);
        obtenerMaterias(rvMateriasS9, 9, textSemestre9, listaMateriasS9);
    }

    private void clasificarMaterias(int semestre, ArrayList<Materia> listMaterias, final RecyclerView recyclerView){
        if(semestre == 1){
            mAdapter1 = new adapterMateria(listMaterias, R.layout.materia, this);
            eventoRecyclerView(mAdapter1, listMaterias, recyclerView);
        }else if(semestre==2){
            mAdapter2 = new adapterMateria(listMaterias, R.layout.materia, this);
            eventoRecyclerView(mAdapter2, listMaterias, recyclerView);
        }else if(semestre==3){
            mAdapter3 = new adapterMateria(listMaterias, R.layout.materia, this);
            eventoRecyclerView(mAdapter3, listMaterias, recyclerView);
        }else if(semestre==4){
            mAdapter4 = new adapterMateria(listMaterias, R.layout.materia, this);
            eventoRecyclerView(mAdapter4, listMaterias, recyclerView);
        }else if(semestre==5){
            mAdapter5 = new adapterMateria(listMaterias, R.layout.materia, this);
            eventoRecyclerView(mAdapter5, listMaterias, recyclerView);
        }else if(semestre==6){
            mAdapter6 = new adapterMateria(listMaterias, R.layout.materia, this);
            eventoRecyclerView(mAdapter6, listMaterias, recyclerView);
        }else if(semestre==7){
            mAdapter7 = new adapterMateria(listMaterias, R.layout.materia, this);
            eventoRecyclerView(mAdapter7, listMaterias, recyclerView);
        }else if(semestre==8){
            mAdapter8 = new adapterMateria(listMaterias, R.layout.materia, this);
            eventoRecyclerView(mAdapter8, listMaterias, recyclerView);
        }else if(semestre==9){
            mAdapter9 = new adapterMateria(listMaterias, R.layout.materia, this);
            eventoRecyclerView(mAdapter9, listMaterias, recyclerView);
        }
    }

    private void eventoRecyclerView(adapterMateria adapter,final ArrayList<Materia> listM, final RecyclerView recyclerView){
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int indice = recyclerView.getChildAdapterPosition(view);
                Toast.makeText(VisualizaMaterias.this, listM.get(indice).getClave(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(VisualizaMaterias.this, DetalleMateria.class);
                intent.putExtra("clave", listM.get(indice).getClave());
                intent.putExtra("nombre", listM.get(indice).getNombre());
                intent.putExtra("nombreCorto",listM.get(indice).getNombreCorto());
                intent.putExtra("plan", listM.get(indice).getPlan());
                intent.putExtra("creditos", listM.get(indice).getCreditos());
                intent.putExtra("horasClase", listM.get(indice).getHorasClase());
                intent.putExtra("horasTeoricas",listM.get(indice).getHorasTeoricas());
                intent.putExtra("horasPracticas", listM.get(indice).getHorasPracticas());
                intent.putExtra("semestre", listM.get(indice).getSemestre());
                intent.putExtra("isEspecialidad", listM.get(indice).getIsEspecialidad());
                intent.putExtra("especialidad", listM.get(indice).getEspecialidad());
                intent.putExtra("isRequerimiento", listM.get(indice).getRequerimientos());
                intent.putExtra("requerimiento1", listM.get(indice).getRequerimiento1());
                intent.putExtra("requerimiento2", listM.get(indice).getRequerimiento2());
                intent.putExtra("requerimiento3", listM.get(indice).getRequerimiento3());
                intent.putExtra("requerimiento4", listM.get(indice).getRequerimiento4());
                intent.putExtra("requerimiento5", listM.get(indice).getRequerimiento5());
                VisualizaMaterias.this.startActivity(intent);
                VisualizaMaterias.this.finish();
            }
        });
        recyclerView.setAdapter(adapter);
    }

}
