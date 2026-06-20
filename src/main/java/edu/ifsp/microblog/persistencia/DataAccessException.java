package edu.ifsp.microblog.persistencia;

// Exceção de runtime que encapsula SQLExceptions da camada de persistência
// Evita que as camadas superiores precisem tratar SQLException
public class DataAccessException extends RuntimeException {
	public DataAccessException(Throwable t) {
		super(t);
	}

	public DataAccessException(String message) {
		super(message);
	}

}
