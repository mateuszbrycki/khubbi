package com.bookkeeper.app.adapter.in.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class User implements UserDetails {

  private UUID id;
  private String fullName;
  private String email;
  private String password;
  private Date createdAt;
  private Date updatedAt;

  public User(
      UUID id, String fullName, String email, String password, Date createdAt, Date updatedAt) {
    this.id = id;
    this.fullName = fullName;
    this.email = email;
    this.password = password;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id)
        && Objects.equals(fullName, user.fullName)
        && Objects.equals(email, user.email)
        && Objects.equals(password, user.password)
        && Objects.equals(createdAt, user.createdAt)
        && Objects.equals(updatedAt, user.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fullName, email, password, createdAt, updatedAt);
  }
}
