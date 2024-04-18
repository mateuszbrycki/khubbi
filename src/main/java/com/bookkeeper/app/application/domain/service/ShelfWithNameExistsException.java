package com.bookkeeper.app.application.domain.service;

public class ShelfWithNameExistsException extends RuntimeException {
    public ShelfWithNameExistsException(String message) {
        super(message);
    }
}
