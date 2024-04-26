package com.bookkeeper.app.adapter.in.web.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

  private static final String TEST_SECRET_KEY =
      "d70ab8aa537f1489e74ad7b1d9f29a826eed255b5a39d043d2e4f47a0479487c";
  private static final String TEST_EMAIL = "test-email";
  private static final int TEST_JWT_EXPIRATION = 3600000;
  private final Map<String, Object> TEST_CLAIMS = Map.of("claim1", "claim1-value");

  @Test
  public void testGeneratingTokenForEmptyEmail() {

    // given
    JwtService underTest = new JwtService(TEST_SECRET_KEY, TEST_JWT_EXPIRATION);

    // when & then
    assertThrows(IllegalArgumentException.class, () -> underTest.generateToken(null));
  }

  @Test
  public void testGeneratingTokenForNullEmail() {

    // given
    JwtService underTest = new JwtService(TEST_SECRET_KEY, TEST_JWT_EXPIRATION);

    // when & then
    assertThrows(IllegalArgumentException.class, () -> underTest.generateToken(""));
  }

  @Test
  public void testGeneratingTokenWithClaimsForEmptyEmail() {

    // given
    JwtService underTest = new JwtService(TEST_SECRET_KEY, TEST_JWT_EXPIRATION);

    // when & then
    assertThrows(IllegalArgumentException.class, () -> underTest.generateToken(TEST_CLAIMS, null));
  }

  @Test
  public void testGeneratingTokenWithClaimsForNullEmail() {

    // given
    JwtService underTest = new JwtService(TEST_SECRET_KEY, TEST_JWT_EXPIRATION);

    // when & then
    assertThrows(IllegalArgumentException.class, () -> underTest.generateToken(TEST_CLAIMS, ""));
  }

  @Test
  public void testEmailRetrievalFromToken() {

    // given
    JwtService underTest = new JwtService(TEST_SECRET_KEY, TEST_JWT_EXPIRATION);
    String token = underTest.generateToken(TEST_EMAIL);

    // when
    String email = underTest.extractEmail(token);

    // then
    assertEquals(TEST_EMAIL, email);
  }

  @Test
  public void testEmailRetrievalFromTokenWithClaims() {

    // given
    JwtService underTest = new JwtService(TEST_SECRET_KEY, TEST_JWT_EXPIRATION);
    String token = underTest.generateToken(TEST_CLAIMS, TEST_EMAIL);

    // when
    String email = underTest.extractEmail(token);

    // then
    assertEquals(TEST_EMAIL, email);
  }

  @Test
  public void testGetExpirationTime() {

    // given
    JwtService underTest = new JwtService(TEST_SECRET_KEY, TEST_JWT_EXPIRATION);

    // when
    long expirationTime = underTest.getExpirationTime();

    // then
    assertEquals(3600000, expirationTime);
  }

  @Test
  public void testTokenGetsExpired() throws InterruptedException {

    // given
    JwtService underTest = new JwtService(TEST_SECRET_KEY, 1000);
    String token = underTest.generateToken(TEST_EMAIL);
    UserDetails userDetails =
        new User(
            UUID.randomUUID(),
            "full-name",
            TEST_EMAIL,
            "password",
            Date.from(Instant.now()),
            Date.from(Instant.now()));

    // then & then
    assertTrue(underTest.isTokenValid(token, userDetails));
    Thread.sleep(1000);
    assertFalse(underTest.isTokenValid(token, userDetails));
  }

  @Test
  public void testExtractClaimsWhenProvidedWhenGeneratingToken() {

    // given
    JwtService underTest = new JwtService(TEST_SECRET_KEY, TEST_JWT_EXPIRATION);
    String token = underTest.generateToken(TEST_CLAIMS, TEST_EMAIL);

    // when
    String claim =
        underTest.extractClaim(token, (Claims claims) -> claims.get("claim1", String.class));

    // then
    assertEquals("claim1-value", claim);
  }

  @Test
  public void testExtractClaimsForNonExistentClaimIfClaimsProvidedWhenGeneratingToken() {

    // given
    JwtService underTest = new JwtService(TEST_SECRET_KEY, TEST_JWT_EXPIRATION);
    String token = underTest.generateToken(TEST_CLAIMS, TEST_EMAIL);

    // when
    String claim =
        underTest.extractClaim(token, (Claims claims) -> claims.get("claim2", String.class));

    // then
    assertNull(claim);
  }

  @Test
  public void testExtractClaimsWhenNotProvidedWhenGeneratingToken() {

    // given
    JwtService underTest = new JwtService(TEST_SECRET_KEY, TEST_JWT_EXPIRATION);
    String token = underTest.generateToken(TEST_EMAIL);

    // when
    String claim =
        underTest.extractClaim(token, (Claims claims) -> claims.get("claim1", String.class));

    // then
    assertNull(claim);
  }
}
