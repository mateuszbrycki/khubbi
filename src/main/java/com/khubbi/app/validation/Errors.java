package com.khubbi.app.validation;

import io.vavr.collection.Seq;
import lombok.Getter;

@Getter
public class Errors {

  private final Class<?> validatedClass;
  private final Seq<String> errors;

  public Errors(Class<?> validatedClass, Seq<String> errors) {
    this.validatedClass = validatedClass;
    this.errors = errors;
  }

  @Getter
  public static class PropertyAwareErrors extends Errors {

    private final String property;

    public PropertyAwareErrors(Class<?> validatedClass, Seq<String> errors, String property) {
      super(validatedClass, errors);
      this.property = property;
    }
  }
}
