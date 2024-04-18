package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.adapter.out.persistance.ShelfInMemoryDatabase;
import io.vavr.collection.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShelfServiceConfiguration {

    @Bean
    ShelfService shelfService() {
        ShelfInMemoryDatabase shelfInMemoryDatabase = new ShelfInMemoryDatabase(List.empty());
        return new ShelfService(shelfInMemoryDatabase, shelfInMemoryDatabase);
    }
}
