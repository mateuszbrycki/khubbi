package com.khubbi.app.adapter.in.web.security;

import com.khubbi.app.adapter.in.web.security.jwt.JwtAuthenticationFilter;
import com.khubbi.app.adapter.in.web.security.jwt.JwtService;
import com.khubbi.app.adapter.in.web.security.refresh.RefreshTokenService;
import com.khubbi.app.adapter.out.persistance.RefreshTokenRepository;
import com.khubbi.app.adapter.out.persistance.UserInMemoryRepository;
import com.khubbi.app.adapter.out.persistance.UserTokenRepository;
import com.khubbi.app.application.domain.model.UserEmail;
import com.khubbi.app.application.port.out.ListUsersPort;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Bean
  JwtService jwtService(
      @Value("${security.jwt.secret-key}") String secretKey,
      @Value("${security.jwt.expiration-time}") long jwtExpiration,
      UserTokenRepository userTokenRepository,
      ListUsersPort listUsersPort) {
    return new JwtService(userTokenRepository, listUsersPort, secretKey, jwtExpiration);
  }

  @Bean
  RefreshTokenService refreshTokenService(
      @Value("${security.refresh-token.expiration-time}") long refreshTokenExpiration,
      RefreshTokenRepository refreshTokenRepository) {
    return new RefreshTokenService(refreshTokenRepository, refreshTokenExpiration);
  }

  @Bean
  SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      JwtAuthenticationFilter jwtAuthenticationFilter,
      AuthenticationProvider authenticationProvider)
      throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(r -> corsConfiguration()))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers("/auth/logout")
                    .authenticated()
                    .requestMatchers("/auth/**")
                    .permitAll()
                    .requestMatchers("/static/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  private CorsConfiguration corsConfiguration() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:3001"));
    corsConfiguration.setAllowedMethods(List.of("POST", "GET", "PUT", "DELETE", "OPTIONS"));
    corsConfiguration.setAllowedHeaders(
        List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "X-XSRF-TOKEN"));
    corsConfiguration.setAllowCredentials(true);
    return corsConfiguration;
  }

  @Bean
  UserDetailsService userDetailsService(UserInMemoryRepository userInMemoryRepository) {
    return username ->
        UserEmail.of(username)
            .toTry()
            .mapTry(
                userEmail ->
                    userInMemoryRepository
                        .findByEmail(userEmail)
                        .map(this::toUserDetails)
                        .getOrElseThrow(() -> new UsernameNotFoundException("User not found")))
            .getOrElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  private UserDetails toUserDetails(com.khubbi.app.application.domain.model.User user) {
    return new User(
        user.id(),
        user.fullName(),
        user.email(),
        user.password(),
        user.createdAt(),
        user.updatedAt());
  }

  @Bean
  BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager authenticationManager(
      org.springframework.security.config.annotation.authentication.configuration
              .AuthenticationConfiguration
          config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }
}
