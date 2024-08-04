package com.example.proyectoaula;

public class Servicio {
    private String idServicio;
    private String nombre;
    private String ciudad;
    private String callePrincipal;
    private String calleSecundaria;
    private String referencia;
    private String tipoServicio;

    // Getters y Setters
    public String getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCallePrincipal() {
        return callePrincipal;
    }

    public void setCallePrincipal(String callePrincipal) {
        this.callePrincipal = callePrincipal;
    }

    public String getCalleSecundaria() {
        return calleSecundaria;
    }

    public void setCalleSecundaria(String calleSecundaria) {
        this.calleSecundaria = calleSecundaria;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    // Nuevo método para obtener el detalle del servicio concatenado en formato HTML
    public String getDetalleServicio() {
        return "<b>Ciudad:</b> " + ciudad +
                "<br><b>Calle Principal:</b> " + callePrincipal +
                "<br><b>Calle Secundaria:</b> " + calleSecundaria +
                "<br><b>Referencia:</b> " + referencia +
                "<br><b>Tipo de Servicio:</b> " + tipoServicio;
    }
}
