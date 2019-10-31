package com.example.controlescolar;

public class PojoMateria {
    String clave, nombre, semestre, creditos, hora;

    public PojoMateria() {
    }

    public PojoMateria(String clave, String nombre, String semestre, String creditos, String hora) {
        this.clave = clave;
        this.nombre = nombre;
        this.semestre = semestre;
        this.creditos = creditos;
        this.hora = hora;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getCreditos() {
        return creditos;
    }

    public void setCreditos(String creditos) {
        this.creditos = creditos;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
