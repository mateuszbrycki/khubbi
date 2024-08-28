package com.bookkeeper.app.application.domain.model;

import com.bookkeeper.app.validation.Errors;
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
    return Validation.valid(new UserPassword(value));
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
