package com.example.controlescolar;

public class PojoMateria {
    String clave, nombre, semestre, creditos, hora, aula, grupo, color;

    public PojoMateria() {
    }

    public PojoMateria(String clave, String nombre, String semestre, String creditos, String hora, String aula, String grupo, String color) {
        this.clave = clave;
        this.nombre = nombre;
        this.semestre = semestre;
        this.creditos = creditos;
        this.hora = hora;
        this.aula = aula;
        this.grupo = grupo;
        this.color = color;
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

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
