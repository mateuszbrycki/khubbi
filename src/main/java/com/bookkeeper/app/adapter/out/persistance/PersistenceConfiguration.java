package com.bookkeeper.app.adapter.out.persistance;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceConfiguration {

    @Bean
    InMemoryUserRepository inMemoryUserRepository() {
        return new InMemoryUserRepository();
    }
}
