package com.khubbi.app.validation;

import io.vavr.collection.Seq;
import lombok.Getter;

@Getter
public class ValidationErrors extends Exception {

  private final Seq<Errors> errors;

  public ValidationErrors(Seq<Errors> errors) {
    super("Validation Errors");
    this.errors = errors;
  }
}
