package com.khubbi.app.validation;

import io.vavr.control.Validation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PropertyAwareValidation<E extends Errors, T> implements Validation<E, T> {

  private final Validation<E, T> validation;
  private final String property;

  public static <E extends Errors, T> PropertyAwareValidation<E, T> of(
      String name, Validation<E, T> validation) {
    return new PropertyAwareValidation<>(validation, name);
  }

  public String property() {
    return property;
  }

  @Override
  public boolean isValid() {
    return validation.isValid();
  }

  @Override
  public boolean isInvalid() {
    return validation.isInvalid();
  }

  @Override
  public T get() {
    return validation.get();
  }

  @Override
  public String stringPrefix() {
    return validation.stringPrefix();
  }

  @Override
  public E getError() {
    return (E)
        new Errors.PropertyAwareErrors(
            validation.getError().validatedClass(), validation.getError().errors(), this.property);
  }

  @Override
  public boolean equals(Object o) {
    return validation.equals(o);
  }

  @Override
  public int hashCode() {
    return validation.hashCode();
  }

  @Override
  public String toString() {
    return validation.toString();
  }
}
