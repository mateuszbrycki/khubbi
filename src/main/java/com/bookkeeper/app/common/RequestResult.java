package com.bookkeeper.app.common;

public interface RequestResult {

    class RequestError implements RequestResult {
        private final String message;

        public RequestError(String message) {
            this.message = message;
        }
    }
}
