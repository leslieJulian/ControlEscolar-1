package com.example.controlescolar;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class adapterMateria extends RecyclerView.Adapter<adapterMateria.viewHolder> implements View.OnClickListener{

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance ();
    private DatabaseReference mDatabaseReference = mDatabase.getReference ();
    private int resource;
    private ArrayList<Materia> listaMaterias;
    private View.OnClickListener listener;
    Activity activity;

    public adapterMateria(){}

    public adapterMateria(ArrayList<Materia> listaMaterias, int resource, Activity activity){
        this.listaMaterias = listaMaterias;
        this.resource = resource;
        this.activity = activity;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        view.setOnClickListener(this);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position) {
        final Materia materia = listaMaterias.get(position);

        holder.claveMateria.setText(holder.claveMateria.getText()+" "+materia.getClave());
        holder.nombreMateria.setText(holder.nombreMateria.getText()+" "+materia.getNombre());
        if(materia.getIsEspecialidad()){
            int color = Color.parseColor("#008577");
            holder.relativeLayout.setBackgroundColor(color);
        }

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(activity, "Eliminar", Toast.LENGTH_SHORT).show();
                mDatabaseReference.child("materias").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for (DataSnapshot ds: dataSnapshot.getChildren()){
                                String key = ds.getKey();
                                String claveM = ds.child("clave").getValue().toString();
                                if(claveM.equals(materia.getClave())){
                                    Toast.makeText(activity, key+" = "+claveM, Toast.LENGTH_SHORT).show();
                                    mDatabaseReference = mDatabase.getReference("materias");
                                    mDatabaseReference.child(key).removeValue();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaMaterias.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        private TextView claveMateria;
        private TextView nombreMateria;
        private Button btnEliminar;
        private RelativeLayout relativeLayout;
        public View view;

        public viewHolder(View view){
            super(view);

            this.view = view;
            this.claveMateria = (TextView) view.findViewById(R.id.claveMateria);
            this.nombreMateria = (TextView) view.findViewById(R.id.nombreMateria);
            this.btnEliminar = (Button) view.findViewById(R.id.btnEliminar);
            this.relativeLayout = (RelativeLayout) view.findViewById(R.id.itemMateria);
        }
    }


}
