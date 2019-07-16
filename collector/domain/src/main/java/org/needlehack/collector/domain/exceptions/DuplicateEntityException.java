package org.needlehack.collector.domain.exceptions;

public class DuplicateEntityException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public DuplicateEntityException(String message) {
        super(message);
    }

    public DuplicateEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
