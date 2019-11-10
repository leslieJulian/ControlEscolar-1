package com.example.controlescolar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

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

        Button visualizar = v.findViewById(R.id.btnVisualizar);

        return v;
    }



}
