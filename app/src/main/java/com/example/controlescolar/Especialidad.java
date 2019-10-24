package com.example.controlescolar;

public class Especialidad {
    String nombre, plan;

    public Especialidad() {
    }

    public Especialidad(String nombre, String plan) {
        this.nombre = nombre;
        this.plan = plan;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
}
