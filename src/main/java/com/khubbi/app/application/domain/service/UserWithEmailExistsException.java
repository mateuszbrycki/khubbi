package com.khubbi.app.application.domain.service;

public class UserWithEmailExistsException extends RuntimeException {
  public UserWithEmailExistsException(String message) {
    super(message);
  }
}
