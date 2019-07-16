package org.needlehack.collector.domain.exceptions;

public class EntityAlreadyExistsException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
