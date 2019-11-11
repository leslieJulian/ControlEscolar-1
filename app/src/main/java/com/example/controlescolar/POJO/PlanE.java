package com.example.controlescolar.POJO;

import java.io.Serializable;

public class PlanE implements Serializable {

    private String nombre;

    public PlanE(String nombre_plan) {

        this.nombre = nombre_plan;
    }

    public PlanE() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}