package com.bookkeeper.app.adapter.in.web;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

import com.bookkeeper.app.adapter.in.web.security.JwtService;
import com.bookkeeper.app.application.domain.service.UserWithEmailExistsException;
import com.bookkeeper.app.application.port.in.AddUserUseCase;
import io.vavr.control.Try;
import java.util.Date;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

  private static final Logger LOG = LogManager.getLogger(AuthenticationController.class);

  private final AddUserUseCase addUserUseCase;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;

  public AuthenticationController(
      AddUserUseCase addUserUseCase,
      JwtService jwtService,
      AuthenticationManager authenticationManager,
      PasswordEncoder passwordEncoder) {
    this.addUserUseCase = addUserUseCase;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/signup")
  public ResponseEntity<?> register(@RequestBody RegisterUserDto registerUserDto) {
    LOG.debug("Received registration request {}", registerUserDto);
    Try<RegisterResponse> user =
        addUserUseCase
            .addUser(
                new AddUserUseCase.AddUserCommand(
                    registerUserDto.getEmail(),
                    passwordEncoder.encode(registerUserDto.getPassword()),
                    registerUserDto.getFullName()))
            .map(
                addUserResult ->
                    new RegisterResponse()
                        .setId(addUserResult.getId())
                        .setEmail(addUserResult.getEmail())
                        .setCreatedAt(addUserResult.getCreatedAt()));

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
    LOG.debug("Received login request {}", loginUserDto);
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginUserDto.getEmail(), loginUserDto.getPassword()));

    if (authentication.isAuthenticated()) {
      LOG.debug("User logged in successfully. Generating JWT token.");
      String jwtToken = jwtService.generateToken(loginUserDto.getEmail());
      LoginResponse loginResponse =
          new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());
      return ResponseEntity.ok(loginResponse);
    }

    LOG.debug("User not logged in.");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
    private String token;

    private long expiresIn;

    public String getToken() {
      return token;
    }

    public LoginResponse setToken(String token) {
      this.token = token;
      return this;
    }

    public long getExpiresIn() {
      return expiresIn;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
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
      return "LoginUserDto{" +
              "email='" + email + '\'' +
              '}';
    }
  }

  public static class RegisterUserDto {
    private String email;

    private String password;

    private String fullName;

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

    public String getFullName() {
      return fullName;
    }

    public void setFullName(String fullName) {
      this.fullName = fullName;
    }

    @Override
    public String toString() {
      return "RegisterUserDto{" +
              "email='" + email + '\'' +
              ", fullName='" + fullName + '\'' +
              '}';
    }
  }
}
