package com.example.controlescolar;

public class Evaluacion {
    String calificacion, periodo, semestre, tipo;

    public Evaluacion() {
    }

    public Evaluacion(String calificacion, String periodo, String semestre, String tipo) {
        this.calificacion = calificacion;
        this.periodo = periodo;
        this.semestre = semestre;
        this.tipo = tipo;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
