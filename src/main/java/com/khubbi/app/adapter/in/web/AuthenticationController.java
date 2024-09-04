package com.khubbi.app.adapter.in.web;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

import com.khubbi.app.adapter.in.web.security.jwt.JwtService;
import com.khubbi.app.adapter.in.web.security.jwt.JwtToken;
import com.khubbi.app.adapter.in.web.security.refresh.RefreshToken;
import com.khubbi.app.adapter.in.web.security.refresh.RefreshTokenService;
import com.khubbi.app.application.domain.model.UserEmail;
import com.khubbi.app.application.domain.model.UserPassword;
import com.khubbi.app.application.domain.service.UserWithEmailExistsException;
import com.khubbi.app.application.port.in.AddUserUseCase;
import io.vavr.API;
import io.vavr.control.Option;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/auth")
@RestController
@AllArgsConstructor
public class AuthenticationController {

  private final AddUserUseCase addUserUseCase;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final RefreshTokenService refreshTokenService;

  @PostMapping("/signup")
  public ResponseEntity<?> register(@RequestBody RegisterUserDto registerUserDto) {
    log.info("Received registration request {}", registerUserDto);

    Try<RegisterResponse> user =
        Validation.combine(
                UserEmail.of(registerUserDto.getEmail()),
                UserPassword.of(passwordEncoder.encode(registerUserDto.getPassword())),
                UserPassword.of(passwordEncoder.encode(registerUserDto.getRepeatedPassword())))
            .ap(API::Tuple)
            .toTry()
            .flatMapTry(tuple -> tuple.apply(addUserUseCase::addUser))
            .map(
                addUserResult ->
                    new RegisterResponse()
                        .setId(addUserResult.id())
                        .setEmail(addUserResult.email())
                        .setCreatedAt(addUserResult.createdAt()));

    return user.fold(
        failure -> {
          HttpStatus status =
              Match(failure)
                  .of(
                      Case($(instanceOf(UserWithEmailExistsException.class)), HttpStatus.CONFLICT),
                      Case($(), HttpStatus.INTERNAL_SERVER_ERROR));
          return new ResponseEntity<>(new RequestResult.RequestError(failure.getMessage()), status);
        },
        result -> new ResponseEntity<>(result, HttpStatus.CREATED));
  }

  @PostMapping("/login")
  public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
    log.info("Received login request {}", loginUserDto);
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginUserDto.getEmail(), loginUserDto.getPassword()));

    if (authentication.isAuthenticated()) {
      log.info("User logged in successfully. Generating JWT token.");
      JwtToken jwtToken = jwtService.generateToken(loginUserDto.getEmail());
      RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginUserDto.getEmail());

      LoginResponse loginResponse =
          new LoginResponse()
              .setJwtToken(
                  new LoginResponse.Token()
                      .setToken(jwtToken.getToken())
                      .setExpiresIn(jwtToken.getExpirationTimeInMillis()))
              .setRefreshToken(
                  new LoginResponse.Token()
                      .setToken(refreshToken.token())
                      .setExpiresIn(refreshToken.getExpirationTimeInMillis()));

      return ResponseEntity.ok(loginResponse);
    }

    log.info("User not logged in.");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @PostMapping("/refreshToken")
  public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {

    Option<RefreshToken> maybeRefreshToken =
        refreshTokenService.findToken(refreshTokenDto.getRefreshToken());
    return maybeRefreshToken
        .filter(refreshTokenService::isTokenValid)
        .map(
            refreshToken -> {
              JwtToken jwtToken = jwtService.generateToken(refreshToken.email());
              return ResponseEntity.ok(
                  new RefreshTokenResponse()
                      .setJwtToken(jwtToken.getToken())
                      .setExpiresIn(jwtToken.getExpirationTimeInMillis()));
            })
        .getOrElse(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
  }

  @GetMapping("/logout")
  // removes all the existing tokens at one go
  // will need to be refactored for multiple devices support
  public ResponseEntity<?> logout(Authentication authentication) {

    if (!authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    String email = authentication.getName();

    return jwtService
        .invalidateUserTokens(email)
        .flatMapTry(result -> refreshTokenService.invalidateUserTokens(email))
        .mapTry(result -> ResponseEntity.ok(null))
        .fold(failure -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build(), success -> success);
  }

  public static class RegisterResponse {
    private UUID id;

    private String email;

    private Date createdAt;

    public UUID getId() {
      return id;
    }

    public RegisterResponse setId(UUID id) {
      this.id = id;
      return this;
    }

    public String getEmail() {
      return email;
    }

    public RegisterResponse setEmail(String email) {
      this.email = email;
      return this;
    }

    public Date getCreatedAt() {
      return createdAt;
    }

    public RegisterResponse setCreatedAt(Date createdAt) {
      this.createdAt = createdAt;
      return this;
    }
  }

  public static class LoginResponse {
    private Token jwtToken;
    private Token refreshToken;

    public Token getJwtToken() {
      return jwtToken;
    }

    public LoginResponse setJwtToken(Token jwtToken) {
      this.jwtToken = jwtToken;
      return this;
    }

    public Token getRefreshToken() {
      return refreshToken;
    }

    public LoginResponse setRefreshToken(Token refreshToken) {
      this.refreshToken = refreshToken;
      return this;
    }

    public static class Token {
      private String token;
      private long expiresIn;

      public String getToken() {
        return token;
      }

      public Token setToken(String token) {
        this.token = token;
        return this;
      }

      public long getExpiresIn() {
        return expiresIn;
      }

      public Token setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
      }
    }
  }

  public static class RefreshTokenResponse {
    private String jwtToken;

    private long expiresIn;

    public String getJwtToken() {
      return jwtToken;
    }

    public RefreshTokenResponse setJwtToken(String jwtToken) {
      this.jwtToken = jwtToken;
      return this;
    }

    public long getExpiresIn() {
      return expiresIn;
    }

    public RefreshTokenResponse setExpiresIn(long expiresIn) {
      this.expiresIn = expiresIn;
      return this;
    }
  }

  public static class LoginUserDto {
    private String email;

    private String password;

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    @Override
    public String toString() {
      return "LoginUserDto{" + "email='" + email + '\'' + '}';
    }
  }

  public static class RegisterUserDto {
    private String email;

    private String password;

    private String repeatedPassword;

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public String getRepeatedPassword() {
      return repeatedPassword;
    }

    public void setRepeatedPassword(String fullName) {
      this.repeatedPassword = fullName;
    }

    @Override
    public String toString() {
      return "RegisterUserDto{"
          + "email='"
          + email
          + '\''
          + ", repeatedPassword='"
          + repeatedPassword
          + '\''
          + '}';
    }
  }

  public static class RefreshTokenDto {
    private String refreshToken;

    public String getRefreshToken() {
      return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
      this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
      return "RefreshTokenDto{" + "refreshToken='" + refreshToken + '\'' + '}';
    }
  }
}
