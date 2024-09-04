package com.khubbi.app.application.domain.model;

public record EventCreator(User value) {

  public static EventCreator of(User value) {
    return new EventCreator(value);
  }
}
