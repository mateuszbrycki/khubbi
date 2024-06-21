package com.bookkeeper.app.adapter.in.web.security.refresh;

import com.bookkeeper.app.adapter.out.persistance.RefreshTokenRepository;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static com.bookkeeper.app.common.Anys.ANY_EMAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

  private final long REFRESH_TOKEN_EXPIRATION = 3600000L;

  @Mock private RefreshTokenRepository refreshTokenRepository;

  @Test
  public void testShouldThrowExceptionWhenEmptyEmail() {

    // given
    RefreshTokenService underTest =
        new RefreshTokenService(refreshTokenRepository, REFRESH_TOKEN_EXPIRATION);

    // when
    assertThrows(IllegalArgumentException.class, () -> underTest.createRefreshToken(null));
  }

  @Test
  public void testShouldCreateTokenForNonEmptyEmail() {

    // given
    RefreshTokenService underTest =
        new RefreshTokenService(refreshTokenRepository, REFRESH_TOKEN_EXPIRATION);

    // when
    RefreshToken refreshToken = underTest.createRefreshToken("email");

    // then
    assertNotNull(refreshToken.token());
    assertEquals(refreshToken.email(), "email");
    assertTrue(
        refreshToken
            .expiration()
            .before(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION + 1)));
    verify(refreshTokenRepository).saveRefreshToken(refreshToken);
  }

  @Test
  public void testShouldReturnEmptyWhenNoTokenFound() {
    // given
    when(refreshTokenRepository.getRefreshTokenByToken(any())).thenReturn(Option.none());

    RefreshTokenService underTest =
        new RefreshTokenService(refreshTokenRepository, REFRESH_TOKEN_EXPIRATION);

    // when
    Option<RefreshToken> refreshToken = underTest.findToken(UUID.randomUUID().toString());

    // then
    assertTrue(refreshToken.isEmpty());
  }

  @Test
  public void testShouldReturnSavedToken() {
    // given
    String token = UUID.randomUUID().toString();
    Date expiration = new Date();

    when(refreshTokenRepository.getRefreshTokenByToken(any()))
        .thenReturn(Option.of(new RefreshToken(token, expiration, ANY_EMAIL)));

    RefreshTokenService underTest =
        new RefreshTokenService(refreshTokenRepository, REFRESH_TOKEN_EXPIRATION);

    // when
    Option<RefreshToken> refreshToken = underTest.findToken(token);

    // then
    assertTrue(refreshToken.isDefined());
    assertEquals(refreshToken.get().token(), token);
    assertEquals(refreshToken.get().email(), ANY_EMAIL);
    assertEquals(refreshToken.get().expiration(), expiration);
  }

  @Test
  public void testShouldDetectExpiredToken() {

    // given
    RefreshTokenService underTest =
        new RefreshTokenService(refreshTokenRepository, REFRESH_TOKEN_EXPIRATION);

    // when
    Boolean tokenValid =
        underTest.isTokenValid(
            new RefreshToken(
                UUID.randomUUID().toString(),
                new Date(Instant.now().toEpochMilli() - REFRESH_TOKEN_EXPIRATION),
                ANY_EMAIL));

    // then
    assertFalse(tokenValid);
  }

  @Test
  public void testShouldDetectValidToken() {

    // given
    RefreshTokenService underTest =
        new RefreshTokenService(refreshTokenRepository, REFRESH_TOKEN_EXPIRATION);

    // when
    Boolean tokenValid =
        underTest.isTokenValid(
            new RefreshToken(
                UUID.randomUUID().toString(),
                new Date(Instant.now().toEpochMilli() + REFRESH_TOKEN_EXPIRATION),
                ANY_EMAIL));

    // then
    assertTrue(tokenValid);
  }

  @Test
  public void shouldRemoveActiveTokens() {
    // given
    when(refreshTokenRepository.removeTokens(any())).thenReturn(true);
    RefreshTokenService underTest =
            new RefreshTokenService(refreshTokenRepository, REFRESH_TOKEN_EXPIRATION);

    // when
    Try<Boolean> result = underTest.invalidateUserTokens(ANY_EMAIL);

    // then
    verify(refreshTokenRepository, times(1)).removeTokens(ANY_EMAIL);
    assertTrue(result.isSuccess());
  }
}
