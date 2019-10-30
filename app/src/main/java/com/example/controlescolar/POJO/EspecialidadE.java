package com.example.controlescolar.POJO;

public class EspecialidadE {
    private String nombre_especialidad;
    private String nombre_plan_estudios;

    public EspecialidadE() {

    }

    public EspecialidadE(String nombre_especialidad, String nombre_plan_estudios) {
        this.nombre_especialidad = nombre_especialidad;
        this.nombre_plan_estudios = nombre_plan_estudios;
    }

    public String getNombre_especialidad() {
        return nombre_especialidad;
    }

    public void setNombre_especialidad(String nombre_especialidad) {
        this.nombre_especialidad = nombre_especialidad;
    }

    public String getNombre_plan_estudios() {
        return nombre_plan_estudios;
    }

    public void setNombre_plan_estudios(String nombre_plan_estudios) {
        this.nombre_plan_estudios = nombre_plan_estudios;
    }

}
