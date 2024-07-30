package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.adapter.out.persistance.PhotoInMemoryDatabase;
import io.vavr.collection.HashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PhotoServiceConfiguration {

  @Bean
  public PhotoService photoService() {
    PhotoInMemoryDatabase photoInMemoryDatabase = new PhotoInMemoryDatabase(HashMap.empty());
    return new PhotoService(photoInMemoryDatabase);
  }
}
