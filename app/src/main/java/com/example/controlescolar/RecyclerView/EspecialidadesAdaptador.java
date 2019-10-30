package com.example.controlescolar.RecyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controlescolar.POJO.PlanE;
import com.example.controlescolar.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class EspecialidadesAdaptador extends RecyclerView.Adapter<EspecialidadesAdaptador.PlanViewHolder> {
    ArrayList<PlanE> listaContactos;
    Activity activity;


    public EspecialidadesAdaptador(ArrayList<PlanE> listaContactos, Activity activity) {
        this.listaContactos = listaContactos;
        this.activity = activity;
      //  Snackbar.make(activity.getCurrentFocus(), listaContactos.size(), Snackbar.LENGTH_LONG).show();
    }


    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_planestudios, parent, false);
        return new EspecialidadesAdaptador.PlanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        final PlanE plan = listaContactos.get(position);

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
