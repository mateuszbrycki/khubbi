package com.khubbi.app.validation;

import io.vavr.collection.Seq;

public class Errors {

  private final Seq<String> errors;

  public Errors(Seq<String> errors) {
    this.errors = errors;
  }
}
