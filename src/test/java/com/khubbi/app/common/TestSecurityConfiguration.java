package com.khubbi.app.common;

import static com.khubbi.app.common.Anys.ANY_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.khubbi.app.adapter.in.web.security.SecurityConfiguration;
import com.khubbi.app.adapter.out.persistance.RefreshTokenRepository;
import com.khubbi.app.adapter.out.persistance.UserInMemoryRepository;
import com.khubbi.app.adapter.out.persistance.UserTokenRepository;
import io.vavr.control.Try;
import io.vavr.jackson.datatype.VavrModule;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(SecurityConfiguration.class)
public class TestSecurityConfiguration {

  @Bean
  public VavrModule vavrModule() {
    return new VavrModule();
  }

  @Bean
  public UserInMemoryRepository userInMemoryRepositoryForUserDetailsService() {
    UserInMemoryRepository mock = mock(UserInMemoryRepository.class);
    when(mock.findByEmail(any())).thenReturn(Try.success(ANY_USER));

    return mock;
  }

  @MockBean UserTokenRepository userTokenRepository;
  @MockBean RefreshTokenRepository refreshTokenRepository;
}
