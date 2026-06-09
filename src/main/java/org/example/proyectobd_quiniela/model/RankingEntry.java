package org.example.proyectobd_quiniela.model;

public class RankingEntry {

    private int idUsuario;
    private String nombreUsuario;
    private int puntajeTotal;
    private int partidosPronosticados;
    private int posicion;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    public void setPuntajeTotal(int puntajeTotal) {
        this.puntajeTotal = puntajeTotal;
    }

    public int getPartidosPronosticados() {
        return partidosPronosticados;
    }

    public void setPartidosPronosticados(int partidosPronosticados) {
        this.partidosPronosticados = partidosPronosticados;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
}