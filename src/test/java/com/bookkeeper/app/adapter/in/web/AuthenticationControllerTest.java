package com.bookkeeper.app.adapter.in.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookkeeper.app.adapter.in.web.security.JwtAuthenticationFilter;
import com.bookkeeper.app.adapter.in.web.security.JwtService;
import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.domain.service.UserWithEmailExistsException;
import com.bookkeeper.app.application.port.in.AddUserUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    value = AuthenticationController.class,
    excludeAutoConfiguration = {
      SecurityAutoConfiguration.class,
      SecurityFilterAutoConfiguration.class
    },
    excludeFilters =
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            value = JwtAuthenticationFilter.class))
class AuthenticationControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private AddUserUseCase addUserUseCase;

  @MockBean private JwtService jwtService;

  @MockBean private AuthenticationManager authenticationManager;

  @MockBean private PasswordEncoder passwordEncoder;

  @Test
  public void shouldReturnConflictWhenAddingUserFailed() throws Exception {
    // given
    when(addUserUseCase.addUser(any()))
        .thenReturn(
            Try.failure(
                new UserWithEmailExistsException("User with a given email already exists")));

    AuthenticationController.RegisterUserDto registerUserDto =
        new AuthenticationController.RegisterUserDto();
    registerUserDto.setEmail("any-email");
    registerUserDto.setFullName("any-fullname");
    registerUserDto.setPassword("any-password");

    // when & then
    this.mockMvc
        .perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registerUserDto)))
        .andDo(print())
        .andExpect(status().isConflict())
        .andExpect(content().string(containsString("User with a given email already exists")));
  }

  @Test
  public void shouldReturnInternalServerErrorWhenAddingUserFailed() throws Exception {
    // given
    when(addUserUseCase.addUser(any()))
        .thenReturn(Try.failure(new RuntimeException("Any exception when adding a user")));

    AuthenticationController.RegisterUserDto registerUserDto =
        new AuthenticationController.RegisterUserDto();
    registerUserDto.setEmail("any-email");
    registerUserDto.setFullName("any-fullname");
    registerUserDto.setPassword("any-password");

    // when & then
    this.mockMvc
        .perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registerUserDto)))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(containsString("Any exception when adding a user")));
  }

  @Test
  public void shouldReturnCreatedWhenAddingUserSucceeded() throws Exception {
    // given
    User user =
        new User(
            UUID.randomUUID(),
            "fullname",
            "email",
            "password",
            Date.from(Instant.now()),
            Date.from(Instant.now()));
    when(addUserUseCase.addUser(any())).thenReturn(Try.success(user));

    AuthenticationController.RegisterUserDto registerUserDto =
        new AuthenticationController.RegisterUserDto();
    registerUserDto.setEmail(user.getEmail());
    registerUserDto.setFullName(user.getFullName());
    registerUserDto.setPassword(user.getPassword());

    // when & then
    this.mockMvc
        .perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registerUserDto)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().string(containsString(user.getId().toString())))
        .andExpect(content().string(containsString(user.getEmail())));
  }

  @Test
  public void shouldReturnUnauthorizedWhenUserNoAuthenticated() throws Exception {
    // given
    when(authenticationManager.authenticate(any())).thenReturn(new TestAuthentication(false));

    AuthenticationController.LoginUserDto loginUserDto =
        new AuthenticationController.LoginUserDto();
    loginUserDto.setEmail("any-email");
    loginUserDto.setPassword("any-password");

    // when & then
    this.mockMvc
        .perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginUserDto)))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void shouldReturnTokenWhenUserGotAuthenticatedSuccessfully() throws Exception {
    // given
    when(authenticationManager.authenticate(any())).thenReturn(new TestAuthentication(true));

    String token = UUID.randomUUID().toString();
    long tokenExpirationTime = Instant.now().toEpochMilli();

    when(jwtService.generateToken(any())).thenReturn(token);
    when(jwtService.getExpirationTime()).thenReturn(tokenExpirationTime);

    AuthenticationController.LoginUserDto loginUserDto =
        new AuthenticationController.LoginUserDto();
    loginUserDto.setEmail("any-email");
    loginUserDto.setPassword("any-password");

    // when & then
    this.mockMvc
        .perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginUserDto)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(token)))
        .andExpect(content().string(containsString(String.valueOf(tokenExpirationTime))));
  }

  private final String asJsonString(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  static class TestAuthentication implements Authentication {

    private final boolean authenticated;

    public TestAuthentication(boolean authenticated) {
      this.authenticated = authenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of();
    }

    @Override
    public Object getCredentials() {
      return null;
    }

    @Override
    public Object getDetails() {
      return null;
    }

    @Override
    public Object getPrincipal() {
      return "";
    }

    @Override
    public boolean isAuthenticated() {
      return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {}

    @Override
    public String getName() {
      return "test-authentication";
    }
  }
}
