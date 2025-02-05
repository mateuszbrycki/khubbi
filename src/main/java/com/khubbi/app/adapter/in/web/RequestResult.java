package com.khubbi.app.adapter.in.web;

import com.khubbi.app.validation.Errors;
import com.khubbi.app.validation.ValidationErrors;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import java.util.function.Function;
import lombok.NonNull;

public interface RequestResult {

  class RequestStringError implements RequestResult {
    @NonNull private final String message;

    public RequestStringError(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }

  class RequestValidationError implements RequestResult {
    @NonNull private final Map<String, Seq<String>> errors;

    public RequestValidationError(@NonNull Map<String, Seq<String>> errors) {
      this.errors = errors;
    }

    public @NonNull Map<String, Seq<String>> getErrors() {
      return errors;
    }

    public static RequestValidationError of(ValidationErrors errors) {
      return new RequestValidationError(
          errors.errors().toMap(error -> toKey.apply(error), error -> error.errors().toList()));
    }

    static Function<Errors, String> toKey =
        (Errors error) ->
            error instanceof Errors.PropertyAwareErrors
                ? ((Errors.PropertyAwareErrors) error).property()
                : error.validatedClass().getSimpleName();
  }
}
