package com.example.controlescolar;

public class Materia {


    private String clave,nombre,plan,especialidad,requerimiento1,requerimiento2,requerimiento3,requerimiento4,requerimiento5,nombreCorto;
    private int creditos,horasClase,horasTeoricas,horasPracticas,semestre;
    private Boolean isEspecialidad, requerimientos;

    public Materia() {
    }

    public Materia(String clave, String nombre, String nombreCorto, String plan, String especialidad, String requerimiento1, String requerimiento2, String requerimiento3, String requerimiento4, String requerimiento5, int creditos, int horasClase, int horasTeoricas, int horasPracticas, int semestre, boolean isEspecialidad, boolean requerimientos) {
        this.clave = clave;
        this.nombre = nombre;
        this.plan = plan;
        this.especialidad = especialidad;
        this.requerimiento1 = requerimiento1;
        this.requerimiento2 = requerimiento2;
        this.requerimiento3 = requerimiento3;
        this.requerimiento4 = requerimiento4;
        this.requerimiento5 = requerimiento5;
        this.creditos = creditos;
        this.horasClase = horasClase;
        this.horasTeoricas = horasTeoricas;
        this.horasPracticas = horasPracticas;
        this.semestre = semestre;
        this.isEspecialidad = isEspecialidad;
        this.requerimientos = requerimientos;
        this.nombreCorto=nombreCorto;
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

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
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

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public int getHorasClase() {
        return horasClase;
    }

    public void setHorasClase(int horasClase) {
        this.horasClase = horasClase;
    }

    public int getHorasTeoricas() {
        return horasTeoricas;
    }

    public void setHorasTeoricas(int horasTeoricas) {
        this.horasTeoricas = horasTeoricas;
    }

    public int getHorasPracticas() {
        return horasPracticas;
    }

    public void setHorasPracticas(int horasPracticas) {
        this.horasPracticas = horasPracticas;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public Boolean getRequerimientos() {
        return requerimientos;
    }

    public void setRequerimientos(Boolean requierimientos) {
        this.requerimientos = requerimientos;
    }

    public Boolean getIsEspecialidad(){
        return isEspecialidad;
    }

    public void setIsEspecialidad(Boolean especialidad) {
        isEspecialidad = especialidad;
    }

    public String getNombreCorto() { return nombreCorto; }

    public void setNombreCorto(String nombreCorto) { this.nombreCorto = nombreCorto; }
}
