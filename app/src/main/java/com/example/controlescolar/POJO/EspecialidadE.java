package com.example.controlescolar.POJO;

import java.io.Serializable;

public class EspecialidadE implements Serializable {
    private String nombre;
    private String plan;
    private String estado;


    public EspecialidadE() {

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

    @Override
    public String toString() {
        return "EspecialidadE{" +
                "nombre='" + nombre + '\'' +
                ", plan='" + plan + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
