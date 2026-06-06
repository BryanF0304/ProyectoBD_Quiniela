package org.example.proyectobd_quiniela.exception;

public class NegocioException extends RuntimeException {

    public NegocioException(String mensaje) {
        super(mensaje);
    }
}