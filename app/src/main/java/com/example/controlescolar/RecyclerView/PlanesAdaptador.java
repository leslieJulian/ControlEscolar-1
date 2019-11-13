package com.example.controlescolar.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controlescolar.Editar_plan_act;
import com.example.controlescolar.POJO.PlanE;
import com.example.controlescolar.R;

import java.util.ArrayList;

public class PlanesAdaptador extends RecyclerView.Adapter<PlanesAdaptador.PlanViewHolder> {
    ArrayList<PlanE> listaContactos;
    Activity activity;
    ArrayList<String> listaKEys;

    public PlanesAdaptador(ArrayList<PlanE> listaContactos, Activity activity, ArrayList<String> listaKeys) {
        this.listaContactos = listaContactos;
        this.activity = activity;
        this.listaKEys = listaKeys;

    }


    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_planestudios, parent, false);
        return new PlanesAdaptador.PlanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        final PlanE plan = listaContactos.get(position);
        final String keyPlan = listaKEys.get(position);
        holder.nombrePlan.setText(plan.getNombre());


        holder.nombrePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pl = new Intent(activity, Editar_plan_act.class);
                pl.putExtra("keyplan", keyPlan);
                pl.putExtra("nplan", plan.getNombre());
                activity.startActivity(pl);
            }
        });
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
