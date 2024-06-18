package com.bookkeeper.app.adapter.out.persistance;

import static com.bookkeeper.app.common.Anys.ANY_EMAIL;
import static org.junit.jupiter.api.Assertions.*;

import com.bookkeeper.app.adapter.in.web.security.refresh.RefreshToken;
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
}
