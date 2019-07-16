package org.needlehack.collector.domain.exceptions;

public class EntityValidationException extends BusinessException {

    private static final long serialVersionUID = 1L;
    protected Object instance;

    public EntityValidationException(String message) {
        super(message);
    }

    public EntityValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityValidationException(String message, Object instance) {
        super(message);
        this.instance = instance;
    }

    public Object getInstance() {
        return instance;
    }
}
