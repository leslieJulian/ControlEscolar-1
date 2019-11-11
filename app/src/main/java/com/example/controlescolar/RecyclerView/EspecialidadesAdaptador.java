package com.example.controlescolar.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controlescolar.EditarEspecialidad;
import com.example.controlescolar.POJO.EspecialidadE;
import com.example.controlescolar.POJO.PlanE;
import com.example.controlescolar.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class EspecialidadesAdaptador extends RecyclerView.Adapter<EspecialidadesAdaptador.PlanViewHolder> {
    ArrayList<EspecialidadE> listaContactos;
    Activity activity;
    ArrayList<String> lista_keys;

    public EspecialidadesAdaptador(ArrayList<EspecialidadE> listaContactos, Activity activity, ArrayList<String> lista) {
        this.listaContactos = listaContactos;
        this.activity = activity;
        this.lista_keys = lista;
        //  Snackbar.make(activity.getCurrentFocus(), listaContactos.size(), Snackbar.LENGTH_LONG).show();
    }


    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_especialidades, parent, false);
        return new EspecialidadesAdaptador.PlanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        final EspecialidadE especialidadd = listaContactos.get(position);
        final String keyEspecialidad = lista_keys.get(position).toString();
        holder.nombreEspecialidad.setText(especialidadd.getNombre());
        holder.nombrePlan.setText(especialidadd.getPlan());
        holder.nombreEspecialidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent c = new Intent(activity, EditarEspecialidad.class);
                c.putExtra("especialidad", especialidadd);
                c.putExtra("key", keyEspecialidad);
                activity.startActivity(c);

            }
        });
        holder.nombrePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent c = new Intent(activity, EditarEspecialidad.class);
                c.putExtra("especialidad", especialidadd);
                c.putExtra("key", keyEspecialidad);
                activity.startActivity(c);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        private TextView nombrePlan;

        private TextView clavePlan, nombreEspecialidad;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreEspecialidad = itemView.findViewById(R.id.tvt_nombreEspecialidad);
            nombrePlan = (TextView) itemView.findViewById(R.id.tvt_claveEspecialidadPlan);

        }
    }


}
