package com.khubbi.app.application.domain.model;

import com.khubbi.app.validation.Errors;
import io.vavr.control.Validation;
import java.util.Objects;
import lombok.Getter;

@ValueObject
@Getter
public class UserPassword {
  private final String value;

  private UserPassword(String value) {
    this.value = value;
  }

  public static Validation<Errors, UserPassword> of(String value) {
    return Validation.combine(validateNotEmpty(value, "Value is empty"), alwaysValid(value))
        .ap((String value1, String value2) -> new UserPassword(value))
        .mapError(errors -> new Errors(UserPassword.class, errors));
  }

  private static Validation<String, String> alwaysValid(String value) {
    return Validation.valid(value);
  }

  private static Validation<String, String> validateNotEmpty(String field, String error) {

    return field != null && !field.isEmpty() ? Validation.valid(field) : Validation.invalid(error);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserPassword that = (UserPassword) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
