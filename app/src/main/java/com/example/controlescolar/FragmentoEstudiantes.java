package com.example.controlescolar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentoEstudiantes extends Fragment {

    public FragmentoEstudiantes() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_estudiantes, container, false);

        Button registrar = v.findViewById(R.id.btnRegistrar);
        registrar.setOnClickListener(reg);


        Button visualizar = v.findViewById(R.id.btnVisualizar);
        visualizar.setOnClickListener(vis);


        return v;
    }

    View.OnClickListener vis = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment este = null;
            este = new VisualizarEstudiante();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.container, este).commit();
        }
    };

    View.OnClickListener reg = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment este = null;
            este = new RegistrarEstudiante();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.container, este).commit();
        }
    };



}
