package com.khubbi.app.validation;

import io.vavr.collection.Seq;
import io.vavr.control.Try;
import io.vavr.control.Validation;

public class ValidationToTry {

  public static <T> Try<T> toTry(Validation<Seq<Errors>, T> validation) {
    return validation.fold(errors -> Try.failure(new ValidationErrors(errors)), Try::success);
  }
}
