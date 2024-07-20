package com.bookkeeper.app.application.domain.service;

public class EventWithNameExistsException extends RuntimeException {
  public EventWithNameExistsException(String message) {
    super(message);
  }
}
