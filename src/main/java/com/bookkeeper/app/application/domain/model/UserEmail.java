package com.bookkeeper.app.application.domain.model;

import com.bookkeeper.app.validation.Errors;
import io.vavr.control.Validation;
import java.util.Objects;
import java.util.regex.Pattern;
import lombok.Getter;

@Getter
@ValueObject
public class UserEmail {

  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

  private static final Pattern NOT_EMPTY_VALUE = Pattern.compile("^[^\\s]*\\S[^\\s]*$");

  private final String value;

  private UserEmail(String value) {
    this.value = value;
  }

  public static Validation<Errors, UserEmail> of(String value) {
    return Validation.combine(
            validateField(value, NOT_EMPTY_VALUE, "Value is empty"),
            validateField(value, EMAIL_PATTERN, "Invalid email"))
        .ap((String value1, String value2) -> new UserEmail(value))
        .mapError(Errors::new);
  }

  private static Validation<String, String> validateField(
      String field, Pattern pattern, String error) {

    return pattern.matcher(field).matches() ? Validation.valid(field) : Validation.invalid(error);
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
