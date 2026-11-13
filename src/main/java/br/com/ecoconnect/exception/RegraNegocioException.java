package br.com.ecoconnect.exception;

import org.springframework.http.HttpStatus;

public class RegraNegocioException extends RuntimeException {

    private final HttpStatus status;

    public RegraNegocioException(String mensagem) {
        this(mensagem, HttpStatus.BAD_REQUEST);
    }

    public RegraNegocioException(String mensagem, HttpStatus status) {
        super(mensagem);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
