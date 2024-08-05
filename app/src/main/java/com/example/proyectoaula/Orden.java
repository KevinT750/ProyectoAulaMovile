package com.example.proyectoaula;

public class Orden {

    private String idOrden;
    private String fechaInicio;
    private String fechaFin;
    private String descripcion;
    private String estado;

    // Getters y Setters

    public String getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(String idOrden) {
        this.idOrden = idOrden;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin != null ? fechaFin : "Aún no se ha finalizado este evento";
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getConcatenatedInfo() {
        return "Fecha Inicio: " + fechaInicio + "\n" +
                "Fecha Fin: " + getFechaFin() + "\n" +
                "Descripción: " + descripcion + "\n" +
                "Estado: " + estado;
    }

    public String getOrdenInfo() {
        return "Orden #: " + idOrden;
    }
}
