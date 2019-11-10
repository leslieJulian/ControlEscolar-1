package com.example.controlescolar;

public class Grupo {
    String aula, cupo, docente, hora, materia, nombre, periodo;

    public Grupo() {
    }

    public Grupo(String aula, String cupo, String docente, String hora, String materia, String nombre, String periodo) {
        this.aula = aula;
        this.cupo = cupo;
        this.docente = docente;
        this.hora = hora;
        this.materia = materia;
        this.nombre = nombre;
        this.periodo = periodo;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getCupo() {
        return cupo;
    }

    public void setCupo(String cupo) {
        this.cupo = cupo;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
