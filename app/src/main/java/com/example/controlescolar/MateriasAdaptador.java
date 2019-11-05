package com.example.controlescolar;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MateriasAdaptador extends RecyclerView.Adapter<MateriasAdaptador.MateriasViewHolder> {
    //Arreglo de materias a cargar
    ArrayList<PojoMateria> materias;
    Activity act;

    public MateriasAdaptador(ArrayList<PojoMateria> materias, Activity activity) {
        this.materias = materias;
        act = activity;
    }

    @NonNull
    @Override
    public MateriasAdaptador.MateriasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_materia, parent, false);
        return new MateriasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MateriasAdaptador.MateriasViewHolder holder, int position) {
        final PojoMateria materia = materias.get(position);
        holder.nombre.setText(materia.getNombre());
        holder.semestre.setText("Semestre: "+materia.getSemestre());
        holder.creditos.setText("Créditos: "+materia.getCreditos());
        holder.clave.setText(materia.getClave());
        holder.hora.setText("Hora: "+materia.getHora());
        holder.seleccion.setChecked(false);

        //Verificamos si ya existe una materia del mismo nombre
        boolean existe = false;
        for(int i=0; i< position; i++){
            if(materia.getClave().equals(materias.get(i).getClave())){
                holder.back.setBackgroundColor(Color.parseColor(materias.get(i).getColor()));
                existe = true;
                break;
            }
        }
        if(!existe){
            holder.back.setBackgroundColor(Color.parseColor(FragmentCargarMaterias.coloresFondo.get(0)));

            for(int i=0; i< materias.size(); i++){
                if((materia.getClave().equals(materias.get(i).getClave())) && (materia.getHora().equals(materias.get(i).getHora()))){
                    materias.get(i).setColor(FragmentCargarMaterias.coloresFondo.get(0));
                    FragmentCargarMaterias.coloresFondo.remove(0);
                }
            }
        }

        holder.seleccion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    boolean traslape = false, mismaMateria = false;
                    //Se verifica si hay traslapes de hora
                    for(int i=0; i<FragmentHorario.claves.size(); i+=2){
                        //Si ya existe una materia en esa hora
                        if((FragmentHorario.claves.get(i+1).equals(materia.getHora()))){
                            traslape = true;
                            break;
                        //Si es la misma materia
                        }else if((FragmentHorario.claves.get(i).equals(materia.getClave()))){
                            mismaMateria = true;
                            break;
                        }
                    }
                    if(!traslape && !mismaMateria){
                        //Si es marcada se agrega al horario
                        FragmentHorario.claves.add(holder.clave.getText().toString());
                        FragmentHorario.claves.add(materia.getHora());
                    }else if(traslape){
                        Toast.makeText(act, "Existe traslape con otra materia", Toast.LENGTH_SHORT).show();
                        holder.seleccion.setChecked(false);
                    }else if(!mismaMateria){
                        //Si es marcada se agrega al horario
                        FragmentHorario.claves.add(holder.clave.getText().toString());
                        FragmentHorario.claves.add(materia.getHora());
                    }else{
                        Toast.makeText(act, "Materia ya seleccionada", Toast.LENGTH_SHORT).show();
                        holder.seleccion.setChecked(false);
                    }
                }else{
                    //Si es deseleccionada la materia se quita del arreglo
                    for(int i=0; i<FragmentHorario.claves.size(); i+=2){
                        if(FragmentHorario.claves.get(i).equals(materia.getClave())){
                            FragmentHorario.claves.remove(i);
                            FragmentHorario.claves.remove(i);
                            break;
                        }
                    }
                }
                FragmentHorario.cargarHorario();
            }
        });
    }

    @Override
    public int getItemCount() {
        return materias.size();
    }

    public static class MateriasViewHolder extends RecyclerView.ViewHolder{
        //Elementos del cardview materia
        TextView clave, hora, nombre, creditos, semestre;
        CheckBox seleccion;
        ConstraintLayout back;
        public MateriasViewHolder(@NonNull View itemView) {
            super(itemView);
            back = itemView.findViewById(R.id.background);
            clave = itemView.findViewById(R.id.claveMateria);
            hora = itemView.findViewById(R.id.horaMateria);
            nombre = itemView.findViewById(R.id.nombreMateria);
            creditos = itemView.findViewById(R.id.creditosMateria);
            semestre = itemView.findViewById(R.id.semestreMateria);
            seleccion = itemView.findViewById(R.id.seleccionMateria);
        }
    }
}
