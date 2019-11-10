package com.example.controlescolar;

public class Especialidad {
    String nombre, plan, estado;

    public Especialidad() {
    }

    public Especialidad(String nombre, String plan, String estado) {
        this.nombre = nombre;
        this.plan = plan;
        this.estado = estado;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
