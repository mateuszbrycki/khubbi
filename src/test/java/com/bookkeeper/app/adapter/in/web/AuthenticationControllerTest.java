package com.bookkeeper.app.adapter.in.web;

import static com.bookkeeper.app.common.Anys.ANY_EMAIL;
import static com.bookkeeper.app.common.JsonUtils.asJsonString;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookkeeper.app.adapter.in.web.security.jwt.JwtService;
import com.bookkeeper.app.adapter.in.web.security.jwt.JwtToken;
import com.bookkeeper.app.adapter.in.web.security.refresh.RefreshToken;
import com.bookkeeper.app.adapter.in.web.security.refresh.RefreshTokenService;
import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.domain.service.UserWithEmailExistsException;
import com.bookkeeper.app.application.port.in.AddUserUseCase;
import com.bookkeeper.app.common.TestSecurityConfiguration;
import io.vavr.control.Try;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@Import(TestSecurityConfiguration.class)
@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private AddUserUseCase addUserUseCase;

  @MockBean private JwtService jwtService;

  @MockBean private AuthenticationManager authenticationManager;

  @MockBean private RefreshTokenService refreshTokenService;

  @Test
  public void shouldReturnConflictWhenAddingUserFailed() throws Exception {
    // given
    when(addUserUseCase.addUser(any(), any(), any()))
        .thenReturn(
            Try.failure(
                new UserWithEmailExistsException("User with a given email already exists")));

    AuthenticationController.RegisterUserDto registerUserDto =
        new AuthenticationController.RegisterUserDto();
    registerUserDto.setEmail(ANY_EMAIL);
    registerUserDto.setPassword("any-password");
    registerUserDto.setRepeatedPassword("any-password");

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
    when(addUserUseCase.addUser(any(), any(), any()))
        .thenReturn(Try.failure(new RuntimeException("Any exception when adding a user")));

    AuthenticationController.RegisterUserDto registerUserDto =
        new AuthenticationController.RegisterUserDto();
    registerUserDto.setEmail(ANY_EMAIL);
    registerUserDto.setPassword("any-password");
    registerUserDto.setRepeatedPassword("any-password");

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
            ANY_EMAIL,
            "password",
            Date.from(Instant.now()),
            Date.from(Instant.now()));
    when(addUserUseCase.addUser(any(), any(), any())).thenReturn(Try.success(user));

    AuthenticationController.RegisterUserDto registerUserDto =
        new AuthenticationController.RegisterUserDto();
    registerUserDto.setEmail(user.email());
    registerUserDto.setPassword(user.password());
    registerUserDto.setRepeatedPassword(user.password());

    // when & then
    this.mockMvc
        .perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registerUserDto)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().string(containsString(user.id().toString())))
        .andExpect(content().string(containsString(user.email())));
  }

  @Test
  public void shouldReturnUnauthorizedWhenUserNoAuthenticated() throws Exception {

    // given
    when(authenticationManager.authenticate(any())).thenReturn(new TestAuthentication(false));

    AuthenticationController.LoginUserDto loginUserDto =
        new AuthenticationController.LoginUserDto();
    loginUserDto.setEmail(ANY_EMAIL);
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

    String jwtToken = UUID.randomUUID().toString();
    Date jwtTokenExpirationTime = new Date();
    when(jwtService.generateToken(any()))
        .thenReturn(new JwtToken(jwtToken, jwtTokenExpirationTime));

    String refreshToken = UUID.randomUUID().toString();
    Date refreshTokenExpirationTime = new Date();
    when(refreshTokenService.createRefreshToken(any()))
        .thenReturn(new RefreshToken(refreshToken, refreshTokenExpirationTime, "any-email"));

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
        .andExpect(content().string(containsString(jwtToken)))
        .andExpect(content().string(containsString(refreshToken)))
        .andExpect(
            content()
                .string(
                    containsString(
                        String.valueOf(jwtTokenExpirationTime.toInstant().toEpochMilli()))))
        .andExpect(
            content()
                .string(
                    containsString(
                        String.valueOf(refreshTokenExpirationTime.toInstant().toEpochMilli()))));
  }

  @Test
  @WithUserDetails(ANY_EMAIL)
  public void shouldReturnBadRequestWhenJwtTokenDeletionFailedDuringLogout() throws Exception {

    // given
    when(jwtService.invalidateUserTokens(any()))
        .thenReturn(Try.failure(new RuntimeException("any deletion failure")));

    // when & then
    this.mockMvc.perform(get("/auth/logout")).andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails(ANY_EMAIL)
  public void shouldReturnBadRequestWhenRefreshTokenDeletionFailedDuringLogout() throws Exception {

    // given
    when(jwtService.invalidateUserTokens(any())).thenReturn(Try.success(true));
    when(refreshTokenService.invalidateUserTokens(eq(ANY_EMAIL)))
        .thenReturn(Try.failure(new RuntimeException("any deletion failure")));

    // when & then
    this.mockMvc.perform(get("/auth/logout")).andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  @WithUserDetails(ANY_EMAIL)
  public void shouldOkWhenUserLoggedOutFailed() throws Exception {

    // given
    when(jwtService.invalidateUserTokens(eq(ANY_EMAIL))).thenReturn(Try.success(true));
    when(refreshTokenService.invalidateUserTokens(eq(ANY_EMAIL))).thenReturn(Try.success(true));

    // when & then
    this.mockMvc.perform(get("/auth/logout")).andDo(print()).andExpect(status().isOk());
  }

  @Test
  public void shouldFailDueToNotLoggedUserDuringLogout() throws Exception {

    // when & then
    this.mockMvc.perform(get("/auth/logout")).andDo(print()).andExpect(status().isForbidden());
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
