package com.bookkeeper.app.application.domain.model;

@ValueObject
public record UserEmail(String value) {

  public static UserEmail of(String value) {
    return new UserEmail(value);
  }
}
