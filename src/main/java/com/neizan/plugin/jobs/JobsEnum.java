package com.neizan.plugin.jobs;

public enum JobsEnum {
    EXCAVADOR("Excavador", "Ganas dinero al excavar bloques como tierra, arena, grava"),
    MINERO("Minero", "Ganas dinero al minar minerales"),
    ASESINO("Asesino", "Ganas dinero al matar mobs"),
    GRANJERO("Granjero", "Ganas dinero al cosechar cultivos"),
    PESCADOR("Pescador", "Ganas dinero al pescar");

    private final String nombre;
    private final String descripcion;

    JobsEnum(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
