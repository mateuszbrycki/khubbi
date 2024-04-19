package com.bookkeeper.app.adapter.in.web;

public interface RequestResult {

  class RequestError implements RequestResult {
    private final String message;

    public RequestError(String message) {
      this.message = message;
    }
  }
}
