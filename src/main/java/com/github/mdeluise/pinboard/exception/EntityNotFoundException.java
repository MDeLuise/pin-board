package com.github.mdeluise.pinboard.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Object id) {
        this("id", String.valueOf(id));
    }


    public EntityNotFoundException(String field, String fieldValue) {
        super(String.format("Entity with %s %s not found", field, fieldValue));
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
