package com.bookkeeper.app.adapter.in.web.security;

import java.util.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@EqualsAndHashCode
public class User implements UserDetails {

  private UUID id; // TODO mateusz.brycki consider introducing UserId value object
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

  public UUID getId() {
    return id;
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
}
