package com.bookkeeper.app.adapter.in.web;

import io.vavr.jackson.datatype.VavrModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfiguration {

  @Bean
  public VavrModule vavrModule() {
    return new VavrModule();
  }
}
