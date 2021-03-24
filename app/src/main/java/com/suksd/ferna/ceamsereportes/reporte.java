package com.suksd.ferna.ceamsereportes;


public class reporte {

    public String usuario;
    public String sector;
    public String fecha;
    public String descripcion;


    public reporte() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public reporte(String usuario, String sector, String fecha, String descripcion) {
        this.usuario = usuario;
        this.sector = sector;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

}
