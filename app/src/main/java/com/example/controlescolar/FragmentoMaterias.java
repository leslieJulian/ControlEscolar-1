package com.example.controlescolar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentoMaterias extends Fragment {

    public FragmentoMaterias(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_materias, container, false);

        Button especialidades = v.findViewById(R.id.btnEspecialidades);

        especialidades.setOnClickListener(esp);
        Button planes = v.findViewById(R.id.btnPlanes);
        planes.setOnClickListener(plan);
        return v;
    }


    View.OnClickListener esp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment este = null;
            este = new Lista_especialidades_act();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.container, este).commit();
        }
    };

    View.OnClickListener plan = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment este = null;
            este = new Lista_plan_estudios();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.container, este).commit();
        }
    };
}
