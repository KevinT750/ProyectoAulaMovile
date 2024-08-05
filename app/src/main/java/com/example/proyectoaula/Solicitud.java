package com.example.proyectoaula;

public class Solicitud {
    private int idSolicitud;
    private int idServicio;
    private String idSoporte;
    private String tipoDano;
    private String horarioAtencion;
    private String fechaEmision;
    private int estado;
    private String detalle;
    private String idCliente;
    private String ciudad;
    private String callePrincipal;
    private String calleSecundaria;
    private String referencia;
    private String tipoServicio;
    private String servicioEstado;
    private String clienteNombre;

    public Solicitud(int idSolicitud, int idServicio, String idSoporte, String tipoDano, String horarioAtencion, String fechaEmision, int estado, String detalle, String idCliente, String ciudad, String callePrincipal, String calleSecundaria, String referencia, String tipoServicio, String servicioEstado, String clienteNombre) {
        this.idSolicitud = idSolicitud;
        this.idServicio = idServicio;
        this.idSoporte = idSoporte;
        this.tipoDano = tipoDano;
        this.horarioAtencion = horarioAtencion;
        this.fechaEmision = fechaEmision;
        this.estado = estado;
        this.detalle = detalle;
        this.idCliente = idCliente;
        this.ciudad = ciudad;
        this.callePrincipal = callePrincipal;
        this.calleSecundaria = calleSecundaria;
        this.referencia = referencia;
        this.tipoServicio = tipoServicio;
        this.servicioEstado = servicioEstado;
        this.clienteNombre = clienteNombre;
    }

    // Getters and setters
    public int getIdSolicitud() { return idSolicitud; }
    public int getIdServicio() { return idServicio; }
    public String getIdSoporte() { return idSoporte; }
    public String getTipoDano() { return tipoDano; }
    public String getHorarioAtencion() { return horarioAtencion; }
    public String getFechaEmision() { return fechaEmision; }
    public int getEstado() { return estado; }
    public String getDetalle() { return detalle; }
    public String getIdCliente() { return idCliente; }
    public String getCiudad() { return ciudad; }
    public String getCallePrincipal() { return callePrincipal; }
    public String getCalleSecundaria() { return calleSecundaria; }
    public String getReferencia() { return referencia; }
    public String getTipoServicio() { return tipoServicio; }
    public String getServicioEstado() { return servicioEstado; }
    public String getClienteNombre() { return clienteNombre; }

    public String getConcatenatedInfo() {
        return "Tipo Daño: " + tipoDano + "\n" +
                "Horario Atención: " + horarioAtencion + "\n" +
                "Fecha Emisión: " + fechaEmision + "\n" +
                "Detalle: " + detalle + "\n" +
                "Cliente: " + idCliente + "\n" +
                "Ciudad: " + ciudad + "\n" +
                "Calle Principal: " + callePrincipal + "\n" +
                "Calle Secundaria: " + calleSecundaria + "\n" +
                "Referencia: " + referencia + "\n" +
                "Tipo Servicio: " + tipoServicio + "\n" +
                "Servicio Estado: " + servicioEstado + "\n" +
                "Cliente Nombre: " + clienteNombre;
    }

    public String getSolicitudInfo() {
        return "Solicitud #: " + idSolicitud;
    }
}
