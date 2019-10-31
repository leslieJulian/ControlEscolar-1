package com.example.controlescolar;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentPagerAdapterEstudiantes extends FragmentPagerAdapter {
    public Context contexto;

    public FragmentPagerAdapterEstudiantes(Context ctx, FragmentManager fm) {
        super(fm);
        contexto = ctx;
    }

    @Override
    public Fragment getItem(int posicion) {
        if(posicion == 0){
            return new FragmentCargarMaterias();
        }else if(posicion == 1){
            return new FragmentHorario();
        }else{
            return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return ("Materias");
            case 1:
                return "Horario";
            default:
                return null;
        }
    }
}

