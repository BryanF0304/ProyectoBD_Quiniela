package org.example.proyectobd_quiniela.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class PartidoForm {

    private Integer idEquipoLocal;
    private Integer idEquipoVisita;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fechaHora;

    public Integer getIdEquipoLocal() {
        return idEquipoLocal;
    }

    public void setIdEquipoLocal(Integer idEquipoLocal) {
        this.idEquipoLocal = idEquipoLocal;
    }

    public Integer getIdEquipoVisita() {
        return idEquipoVisita;
    }

    public void setIdEquipoVisita(Integer idEquipoVisita) {
        this.idEquipoVisita = idEquipoVisita;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}