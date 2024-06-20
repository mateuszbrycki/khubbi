package com.bookkeeper.app.adapter.out.persistance;

import static com.bookkeeper.app.common.Anys.ANY_EMAIL;
import static com.bookkeeper.app.common.Anys.ANY_SECURITY_USER;
import static org.junit.jupiter.api.Assertions.*;

import io.vavr.collection.HashMap;
import org.junit.jupiter.api.Test;

class UserTokenRepositoryTest {

  @Test
  public void shouldReturnFalseWhenListOfUsersIsEmpty() {

    // given
    UserTokenRepository underTest = new UserTokenRepository(HashMap.empty());

    // when
    Boolean tokenActive = underTest.isTokenActive(ANY_SECURITY_USER, "any-token");

    // then
    assertFalse(tokenActive);
  }

  @Test
  public void shouldReturnFalseWhenActiveTokenAndCheckedAreNorEqual() {

    // given
    UserTokenRepository underTest =
        new UserTokenRepository(HashMap.of(ANY_SECURITY_USER, "any-token-1"));

    // when
    Boolean tokenActive = underTest.isTokenActive(ANY_SECURITY_USER, "any-token-2");

    // then
    assertFalse(tokenActive);
  }

  @Test
  public void shouldReturnTrueWhenActiveTokenAndCheckedAreEqual() {

    // given
    UserTokenRepository underTest =
        new UserTokenRepository(HashMap.of(ANY_SECURITY_USER, "any-token-1"));

    // when
    Boolean tokenActive = underTest.isTokenActive(ANY_SECURITY_USER, "any-token-1");

    // then
    assertTrue(tokenActive);
  }

  @Test
  public void shouldSetTokenWhenListOfUsersIsEmpty() {

    // given
    UserTokenRepository underTest = new UserTokenRepository(HashMap.empty());

    // when
    underTest.refreshToken(ANY_SECURITY_USER, "any-token-1");

    // then
    assertTrue(underTest.isTokenActive(ANY_SECURITY_USER, "any-token-1"));
  }

  @Test
  public void shouldOverrideOldToken() {

    // given
    UserTokenRepository underTest =
        new UserTokenRepository(HashMap.of(ANY_SECURITY_USER, "any-token-1"));

    // when
    underTest.refreshToken(ANY_SECURITY_USER, "any-token-2");

    // then
    assertFalse(underTest.isTokenActive(ANY_SECURITY_USER, "any-token-1"));
    assertTrue(underTest.isTokenActive(ANY_SECURITY_USER, "any-token-2"));
  }

  @Test
  public void shouldNotFailWhenRemovingNonExistentToken() {
    // given
    UserTokenRepository underTest =
            new UserTokenRepository(HashMap.empty());

    // when
    Boolean result = underTest.removeTokens(ANY_EMAIL);

    // then
    assertTrue(result);
  }

  @Test
  public void shouldRemoveExistingTokens() {
    // given
    UserTokenRepository underTest =
            new UserTokenRepository(HashMap.of(ANY_SECURITY_USER, "any-token-1"));
    // then
    Boolean isTokenActive = underTest.isTokenActive(ANY_SECURITY_USER, "any-token-1");
    assertTrue(isTokenActive);

    // when
    Boolean result = underTest.removeTokens(ANY_EMAIL);

    // then
    assertTrue(result);
    isTokenActive = underTest.isTokenActive(ANY_SECURITY_USER, "any-token-1");
    assertFalse(isTokenActive);
  }
}
