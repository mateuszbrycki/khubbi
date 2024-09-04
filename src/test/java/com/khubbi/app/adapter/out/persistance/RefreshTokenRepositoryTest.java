package com.khubbi.app.adapter.out.persistance;

import static com.khubbi.app.common.Anys.ANY_EMAIL;
import static org.junit.jupiter.api.Assertions.*;

import com.khubbi.app.adapter.in.web.security.refresh.RefreshToken;
import io.vavr.collection.HashMap;
import io.vavr.control.Option;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RefreshTokenRepositoryTest {

  @Test
  public void testShouldNotFindNonExistentToken() {
    // given
    RefreshTokenRepository underTest = new RefreshTokenRepository(HashMap.empty());

    // when
    Option<RefreshToken> refreshToken = underTest.getRefreshTokenByToken("none-existent-token");

    // then
    assertTrue(refreshToken.isEmpty());
  }

  @Test
  public void testShouldNotFindExistentToken() {
    // given
    String token = UUID.randomUUID().toString();
    Date expiration = new Date();
    RefreshTokenRepository underTest =
        new RefreshTokenRepository(
            HashMap.of(token, new RefreshToken(token, expiration, ANY_EMAIL)));

    // when
    Option<RefreshToken> refreshToken = underTest.getRefreshTokenByToken(token);

    // then
    assertFalse(refreshToken.isEmpty());
    assertEquals(refreshToken.get().token(), token);
    assertEquals(refreshToken.get().expiration(), expiration);
    assertEquals(refreshToken.get().email(), ANY_EMAIL);
  }

  @Test
  public void testShouldSaveToken() {

    // given
    String token = UUID.randomUUID().toString();
    Date expiration = new Date();
    RefreshTokenRepository underTest = new RefreshTokenRepository(HashMap.empty());

    // when
    underTest.saveRefreshToken(new RefreshToken(token, expiration, ANY_EMAIL));
    Option<RefreshToken> refreshToken = underTest.getRefreshTokenByToken(token);

    // then
    assertTrue(refreshToken.isDefined());
    assertEquals(refreshToken.get(), new RefreshToken(token, expiration, ANY_EMAIL));
  }

  @Test
  public void testShouldOverrideToken() {
    // given
    String token = UUID.randomUUID().toString();
    String token2 = UUID.randomUUID().toString();

    Date expiration = new Date();
    RefreshTokenRepository underTest = new RefreshTokenRepository(HashMap.empty());

    // when
    underTest.saveRefreshToken(new RefreshToken(token, expiration, ANY_EMAIL));
    underTest.saveRefreshToken(new RefreshToken(token2, expiration, ANY_EMAIL));
    Option<RefreshToken> refreshToken = underTest.getRefreshTokenByToken(token);
    Option<RefreshToken> refreshToken2 = underTest.getRefreshTokenByToken(token2);

    // then
    assertTrue(refreshToken.isDefined());
    assertEquals(refreshToken.get(), new RefreshToken(token, expiration, ANY_EMAIL));
    assertTrue(refreshToken2.isDefined());
    assertEquals(refreshToken2.get(), new RefreshToken(token2, expiration, ANY_EMAIL));
  }

  @Test
  public void testShouldNotFailWhenRemovingNonExistentToken() {
    // given
    String token = UUID.randomUUID().toString();
    RefreshTokenRepository underTest = new RefreshTokenRepository(HashMap.empty());

    // when
    underTest.removeTokens(ANY_EMAIL);

    // then
    assertTrue(underTest.getRefreshTokenByToken(token).isEmpty());
  }

  @Test
  public void testShouldRemoveExistingToken() {
    // given
    String token = UUID.randomUUID().toString();
    Date expiration = new Date();

    RefreshTokenRepository underTest =
        new RefreshTokenRepository(
            HashMap.of(token, new RefreshToken(token, expiration, ANY_EMAIL)));
    // when
    Option<RefreshToken> refreshTokenByToken = underTest.getRefreshTokenByToken(token);
    assertTrue(refreshTokenByToken.isDefined());
    assertEquals(refreshTokenByToken.get().token(), token);

    // when
    underTest.removeTokens(ANY_EMAIL);

    // then
    assertTrue(underTest.getRefreshTokenByToken(token).isEmpty());
  }

  @Test
  public void testShouldRemoveExistingTokensWhenMultipleForUserExist() {
    // given
    String token1 = UUID.randomUUID().toString();
    String token2 = UUID.randomUUID().toString();
    Date expiration = new Date();

    RefreshTokenRepository underTest =
        new RefreshTokenRepository(
            HashMap.of(
                token1,
                new RefreshToken(token1, expiration, ANY_EMAIL),
                token2,
                new RefreshToken(token2, expiration, ANY_EMAIL)));
    // when
    Option<RefreshToken> refreshTokenByToken1 = underTest.getRefreshTokenByToken(token1);
    assertTrue(refreshTokenByToken1.isDefined());
    assertEquals(refreshTokenByToken1.get().token(), token1);

    Option<RefreshToken> refreshTokenByToken2 = underTest.getRefreshTokenByToken(token2);
    assertTrue(refreshTokenByToken2.isDefined());
    assertEquals(refreshTokenByToken2.get().token(), token2);

    // when
    underTest.removeTokens(ANY_EMAIL);

    // then
    assertTrue(underTest.getRefreshTokenByToken(token1).isEmpty());
    assertTrue(underTest.getRefreshTokenByToken(token2).isEmpty());
  }
}
