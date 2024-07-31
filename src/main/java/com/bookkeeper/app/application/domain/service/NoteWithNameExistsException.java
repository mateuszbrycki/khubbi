package com.bookkeeper.app.application.domain.service;

public class NoteWithNameExistsException extends RuntimeException {
  public NoteWithNameExistsException(String message) {
    super(message);
  }
}
