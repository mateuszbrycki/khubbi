package com.bookkeeper.app.application.domain.service;

public class UserWithEmailExistsException extends RuntimeException {
  public UserWithEmailExistsException(String message) {
    super(message);
  }
}
