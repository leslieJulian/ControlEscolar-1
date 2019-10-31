package com.example.controlescolar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class FragmentHorario extends Fragment {
    View v;

    public FragmentHorario(){

    }
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ver_horario, container, false);
        return v;
    }
}
