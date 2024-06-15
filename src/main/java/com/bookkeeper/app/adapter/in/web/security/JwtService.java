package com.bookkeeper.app.adapter.in.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtService {

  public static class Token {
    private final String token;
    private final Date expirationTime;

    public Token(String token, Date expirationTime) {
      this.token = token;
      this.expirationTime = expirationTime;
    }

    public String getToken() {
      return token;
    }

    public Date getExpirationTime() {
      return expirationTime;
    }

    public long getExpirationTimeInMillis() {
      return this.getExpirationTime().toInstant().toEpochMilli();
    }
  }

  private static final Logger LOG = LogManager.getLogger(JwtService.class);

  private final String secretKey;
  private final long jwtExpiration;

  public JwtService(String secretKey, long jwtExpiration) {
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

  public Token generateToken(String email) {
    return generateToken(new HashMap<>(), email);
  }

  public Token generateToken(Map<String, Object> extraClaims, String email) {
    return buildToken(extraClaims, email, jwtExpiration);
  }

  public long getExpirationTime() {
    return jwtExpiration;
  }

  private Token buildToken(Map<String, Object> extraClaims, String email, long expiration) {

    if (email == null || email.isEmpty()) {
      throw new IllegalArgumentException("Email cannot be null or empty");
    }

    Date expirationTime = new Date(System.currentTimeMillis() + expiration);
    String token = Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(email)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(expirationTime)
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();

    LOG.info("Building JWT token for {} with expiration {}", email, expirationTime.toString());
    return new Token(token, expirationTime);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    try {
      LOG.info("Validating JWT token for {} ", userDetails.getUsername());

      final String email = extractEmail(token);
      return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    } catch (ExpiredJwtException exception) {
      System.err.println(exception.getMessage());
      return false;
    }
  }

  private boolean isTokenExpired(String token) {
    boolean isValid = extractExpiration(token).before(new Date());
    LOG.info("Validating JWT token expiration time. Token valid: {} ", isValid);
    return isValid;
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
}
