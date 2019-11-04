package com.example.controlescolar;

public class Materia {
    String clave, creditos, especialidad, horasClase, horasPracticas, horasTeoricas, nombre, nombreCorto, plan, requerimiento1, requerimiento2, requerimiento3, requerimiento4, requerimiento5, semestre;

    public Materia() {
    }

    public Materia(String clave, String creditos, String especialidad, String horasClase, String horasPracticas, String horasTeoricas, String nombre, String nombreCorto, String plan, String requerimiento1, String requerimiento2, String requerimiento3, String requerimiento4, String requerimiento5, String semestre) {
        this.clave = clave;
        this.creditos = creditos;
        this.especialidad = especialidad;
        this.horasClase = horasClase;
        this.horasPracticas = horasPracticas;
        this.horasTeoricas = horasTeoricas;
        this.nombre = nombre;
        this.nombreCorto = nombreCorto;
        this.plan = plan;
        this.requerimiento1 = requerimiento1;
        this.requerimiento2 = requerimiento2;
        this.requerimiento3 = requerimiento3;
        this.requerimiento4 = requerimiento4;
        this.requerimiento5 = requerimiento5;
        this.semestre = semestre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCreditos() {
        return creditos;
    }

    public void setCreditos(String creditos) {
        this.creditos = creditos;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getHorasClase() {
        return horasClase;
    }

    public void setHorasClase(String horasClase) {
        this.horasClase = horasClase;
    }

    public String getHorasPracticas() {
        return horasPracticas;
    }

    public void setHorasPracticas(String horasPracticas) {
        this.horasPracticas = horasPracticas;
    }

    public String getHorasTeoricas() {
        return horasTeoricas;
    }

    public void setHorasTeoricas(String horasTeoricas) {
        this.horasTeoricas = horasTeoricas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getRequerimiento1() {
        return requerimiento1;
    }

    public void setRequerimiento1(String requerimiento1) {
        this.requerimiento1 = requerimiento1;
    }

    public String getRequerimiento2() {
        return requerimiento2;
    }

    public void setRequerimiento2(String requerimiento2) {
        this.requerimiento2 = requerimiento2;
    }

    public String getRequerimiento3() {
        return requerimiento3;
    }

    public void setRequerimiento3(String requerimiento3) {
        this.requerimiento3 = requerimiento3;
    }

    public String getRequerimiento4() {
        return requerimiento4;
    }

    public void setRequerimiento4(String requerimiento4) {
        this.requerimiento4 = requerimiento4;
    }

    public String getRequerimiento5() {
        return requerimiento5;
    }

    public void setRequerimiento5(String requerimiento5) {
        this.requerimiento5 = requerimiento5;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }
}