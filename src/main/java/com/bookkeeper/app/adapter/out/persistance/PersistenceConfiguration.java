package com.bookkeeper.app.adapter.out.persistance;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceConfiguration {

    @Bean
    UserInMemoryRepository inMemoryUserRepository() {
        return new UserInMemoryRepository();
    }
}
