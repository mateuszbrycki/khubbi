package com.khubbi.app.validation;

import static com.khubbi.app.validation.ValidationToTry.toTry;
import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import org.junit.jupiter.api.Test;

class ValidationToTryTest {

  @Test
  public void shouldReturnSuccessOnValid() {

    // given
    String value = "Any Value";
    Validation<Seq<Errors>, String> validation = Validation.valid(value);

    // when
    Try<String> result = toTry(validation);

    // then
    assertThat(result).isSuccess();
    assertThat(result).contains(value);
  }

  @Test
  public void shouldReturnFailureOnInvalid() {

    // given
    Errors validationErrors = new Errors(String.class, List.of("error-1", "error-2"));
    Validation<Seq<Errors>, String> validation = Validation.invalid(List.of(validationErrors));

    // when
    Try<String> result = toTry(validation);

    // then
    assertThat(result).isFailure();
    assertThat(result).failBecauseOf(ValidationErrors.class);
  }
}
