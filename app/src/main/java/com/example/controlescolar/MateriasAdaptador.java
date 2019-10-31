package com.example.controlescolar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

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
    public void onBindViewHolder(@NonNull MateriasAdaptador.MateriasViewHolder holder, int position) {
        PojoMateria materia = materias.get(position);
        holder.nombre.setText(materia.getNombre());
        holder.semestre.setText("Semestre: "+materia.getSemestre());
        holder.creditos.setText("Créditos: "+materia.getCreditos());
        holder.clave.setText(materia.getClave());
        holder.hora.setText("Hora: "+materia.getHora());
        holder.seleccion.setChecked(false);
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
