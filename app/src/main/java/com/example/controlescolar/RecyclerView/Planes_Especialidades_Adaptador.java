package com.example.controlescolar.RecyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controlescolar.POJO.EspecialidadE;
import com.example.controlescolar.R;

import java.util.ArrayList;

public class Planes_Especialidades_Adaptador extends RecyclerView.Adapter<Planes_Especialidades_Adaptador.PlanViewHolder> {
    ArrayList<EspecialidadE> listaContactos;
    Activity activity;


    public Planes_Especialidades_Adaptador(ArrayList<EspecialidadE> listaContactos, Activity activity) {
        this.listaContactos = listaContactos;
        this.activity = activity;
    }


    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_planestudios, parent, false);
        return new Planes_Especialidades_Adaptador.PlanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        final EspecialidadE plan= listaContactos.get(position);
        holder.nombrePlan.setText(plan.getNombre());

    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        private TextView nombrePlan;

        private TextView clavePlan;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            nombrePlan = (TextView) itemView.findViewById(R.id.tvt_nombre);

        }
    }


}
