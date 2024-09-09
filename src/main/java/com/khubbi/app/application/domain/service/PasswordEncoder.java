package com.khubbi.app.application.domain.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class PasswordEncoder {

  private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

  public String encode(String password) {
    return passwordEncoder.encode(password);
  }
}
