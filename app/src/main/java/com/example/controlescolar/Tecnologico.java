package com.example.controlescolar;

public class Tecnologico {
    String lema, nombre, periodoactual;

    public Tecnologico() {
    }

    public Tecnologico(String lema, String nombre, String periodoactual) {
        this.lema = lema;
        this.nombre = nombre;
        this.periodoactual = periodoactual;
    }

    public String getLema() {
        return lema;
    }

    public void setLema(String lema) {
        this.lema = lema;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPeriodoactual() {
        return periodoactual;
    }

    public void setPeriodoactual(String periodoactual) {
        this.periodoactual = periodoactual;
    }
}
