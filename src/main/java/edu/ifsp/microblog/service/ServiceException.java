package edu.ifsp.microblog.service;

// Exceção lançada pela camada de serviço.

public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }
}