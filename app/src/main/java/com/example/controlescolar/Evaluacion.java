package com.example.controlescolar;

public class Evaluacion {
    String id, calificacion, tipo;

    public Evaluacion() {
    }

    public Evaluacion(String id, String calificacion, String tipo) {
        this.id = id;
        this.calificacion = calificacion;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
