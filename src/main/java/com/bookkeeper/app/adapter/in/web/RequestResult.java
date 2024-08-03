package com.bookkeeper.app.adapter.in.web;

import lombok.NonNull;

public interface RequestResult {

  class RequestError implements RequestResult {
    @NonNull private final String message;

    public RequestError(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }
}
