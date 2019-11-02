package com.example.controlescolar;

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
import androidx.recyclerview.widget.RecyclerView;

public class MateriasAdaptador extends RecyclerView.Adapter<MateriasAdaptador.MateriasViewHolder> {
    //Arreglo de materias a cargar
    ArrayList<PojoMateria> materias;

    public MateriasAdaptador(ArrayList<PojoMateria> materias) {
        this.materias = materias;
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
        holder.seleccion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    boolean traslape = false;
                    //Se verifica si hay traslapes de hora
                    for(int i=0; i<FragmentHorario.claves.size(); i+=2){
                        //Si ya existe una materia en esa hora o si es la misma materia
                        if((FragmentHorario.claves.get(i+1).equals(materia.getHora()))||(FragmentHorario.claves.get(i).equals(materia.getClave()))){
                            traslape = true;
                            break;
                        }
                    }
                    if(!traslape){
                        //Si es marcada se agrega al horario
                        FragmentHorario.claves.add(holder.clave.getText().toString());
                        FragmentHorario.claves.add(materia.getHora());
                    }else{
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
        public MateriasViewHolder(@NonNull View itemView) {
            super(itemView);
            clave = itemView.findViewById(R.id.claveMateria);
            hora = itemView.findViewById(R.id.horaMateria);
            nombre = itemView.findViewById(R.id.nombreMateria);
            creditos = itemView.findViewById(R.id.creditosMateria);
            semestre = itemView.findViewById(R.id.semestreMateria);
            seleccion = itemView.findViewById(R.id.seleccionMateria);
        }
    }
}
