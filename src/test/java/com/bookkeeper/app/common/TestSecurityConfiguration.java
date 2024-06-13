package com.bookkeeper.app.common;

import static com.bookkeeper.app.common.Anys.ANY_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bookkeeper.app.adapter.in.web.security.SecurityConfiguration;
import com.bookkeeper.app.adapter.out.persistance.UserInMemoryRepository;
import io.vavr.control.Try;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(SecurityConfiguration.class)
public class TestSecurityConfiguration {

  @Bean
  public UserInMemoryRepository userInMemoryRepositoryForUserDetailsService() {
    UserInMemoryRepository mock = mock(UserInMemoryRepository.class);
    when(mock.findByEmail(any())).thenReturn(Try.success(ANY_USER));

    return mock;
  }
}
