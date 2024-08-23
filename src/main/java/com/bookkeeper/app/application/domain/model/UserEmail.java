package com.bookkeeper.app.application.domain.model;

import io.vavr.control.Validation;
import java.util.Objects;
import lombok.Getter;

@Getter
@ValueObject
public class UserEmail {
  private final String value;

  private UserEmail(String value) {
    this.value = value;
  }

  public static Validation<String, UserEmail> of(String value) {
    return validateField(value, "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", "Invalid email")
        .map(result -> new UserEmail(value));
  }

  private static Validation<String, String> validateField(
      String field, String pattern, String error) {

    return Validation.valid(field);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserEmail userEmail = (UserEmail) o;
    return Objects.equals(value, userEmail.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  @Override
  public String toString() {
    return "UserEmail{" + value + '}';
  }
}
