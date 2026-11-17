package br.com.fiap.exception;
public class PersistenciaException extends RuntimeException {
    public PersistenciaException(String message, Throwable cause) { super(message, cause); }
}
