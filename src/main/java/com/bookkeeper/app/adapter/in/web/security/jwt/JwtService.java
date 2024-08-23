package com.bookkeeper.app.adapter.in.web.security.jwt;

import com.bookkeeper.app.adapter.in.web.security.User;
import com.bookkeeper.app.adapter.out.persistance.UserTokenRepository;
import com.bookkeeper.app.application.domain.model.UserEmail;
import com.bookkeeper.app.application.port.out.ListUsersPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.vavr.control.Try;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public class JwtService {

  private final String secretKey;
  private final long jwtExpiration;
  private final UserTokenRepository userTokenRepository;
  private final ListUsersPort listUsersPort;

  public JwtService(
      UserTokenRepository userTokenRepository,
      ListUsersPort listUsersPort,
      String secretKey,
      long jwtExpiration) {
    this.userTokenRepository = userTokenRepository;
    this.listUsersPort = listUsersPort;
    this.secretKey = secretKey;
    this.jwtExpiration = jwtExpiration;
  }

  public String extractEmail(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public JwtToken generateToken(String email) {
    return generateToken(new HashMap<>(), email);
  }

  public JwtToken generateToken(Map<String, Object> extraClaims, String email) {
    JwtToken jwtToken = buildToken(extraClaims, email, jwtExpiration);
    markTokenAsActive(email, jwtToken);

    return jwtToken;
  }

  public long getExpirationTime() {
    return jwtExpiration;
  }

  private JwtToken buildToken(Map<String, Object> extraClaims, String email, long expiration) {

    if (email == null || email.isEmpty()) {
      throw new IllegalArgumentException("Email cannot be null or empty");
    }

    Date expirationTime = new Date(System.currentTimeMillis() + expiration);
    String token =
        Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(email)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(expirationTime)
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();

    log.info("Building JWT token for {} with expiration {}", email, expirationTime.toString());
    return new JwtToken(token, expirationTime);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    try {
      log.info("Validating JWT token for {} ", userDetails.getUsername());

      final String email = extractEmail(token);
      return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    } catch (ExpiredJwtException exception) {
      System.err.println(exception.getMessage());
      return false;
    }
  }

  private boolean isTokenExpired(String token) {
    boolean isExpired = extractExpiration(token).before(new Date());
    log.info("Validating JWT token expiration time. Token expired: {} ", isExpired);
    return isExpired;
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private void markTokenAsActive(String email, JwtToken token) {
    log.info("Marking JWT Token as active for {}", email);
    UserEmail.of(email)
        .toTry()
        .map(
            userEmail ->
                this.listUsersPort
                    .findByEmail(userEmail)
                    .mapTry(this::toUserDetails)
                    .andThen(
                        userDetails ->
                            this.userTokenRepository.refreshToken(userDetails, token.getToken())));
  }

  public Try<Boolean> invalidateUserTokens(String email) {
    log.info("Invalidating JWT Tokens for {}", email);
    return Try.of(() -> this.userTokenRepository.removeTokens(email));
  }

  private UserDetails toUserDetails(com.bookkeeper.app.application.domain.model.User user) {
    return new User(
        user.id(),
        user.fullName(),
        user.email(),
        user.password(),
        user.createdAt(),
        user.updatedAt());
  }
}
