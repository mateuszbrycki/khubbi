package com.bookkeeper.app.adapter.in.web;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<RequestResult.RequestError> handleSecurityException(Exception exception) {

    if (exception instanceof BadCredentialsException) {
      return new ResponseEntity<>(
          new RequestResult.RequestError("The username or password is incorrect"),
          HttpStatus.UNAUTHORIZED);
    }

    if (exception instanceof AccountStatusException) {
      return new ResponseEntity<>(
          new RequestResult.RequestError("The account is locked"), HttpStatus.FORBIDDEN);
    }

    if (exception instanceof AccessDeniedException) {
      return new ResponseEntity<>(
          new RequestResult.RequestError("You are not authorized to access this resource"),
          HttpStatus.FORBIDDEN);
    }

    if (exception instanceof SignatureException) {
      return new ResponseEntity<>(
          new RequestResult.RequestError("The JWT signature is invalid"), HttpStatus.FORBIDDEN);
    }

    if (exception instanceof ExpiredJwtException) {
      return new ResponseEntity<>(
          new RequestResult.RequestError("The JWT token has expired"), HttpStatus.FORBIDDEN);
    }

    return new ResponseEntity<>(
        new RequestResult.RequestError("Unknown internal server error."),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
