package com.bookkeeper.app.adapter.in.web.security.jwt;

import com.bookkeeper.app.adapter.out.persistance.UserTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger LOG = LogManager.getLogger(JwtAuthenticationFilter.class);

  private final HandlerExceptionResolver handlerExceptionResolver;

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final UserTokenRepository userTokenRepository;

  public JwtAuthenticationFilter(
      JwtService jwtService,
      UserDetailsService userDetailsService,
      UserTokenRepository userTokenRepository,
      HandlerExceptionResolver handlerExceptionResolver) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
    this.userTokenRepository = userTokenRepository;
    this.handlerExceptionResolver = handlerExceptionResolver;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      final String jwt = authHeader.substring(7);
      final String userEmail = jwtService.extractEmail(jwt);

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (userEmail != null && authentication == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        Boolean isTokenActive = userTokenRepository.isTokenActive(userDetails, jwt);
        Boolean isTokenValid = jwtService.isTokenValid(jwt, userDetails);
        LOG.info(
            "Token for {} is valid: {} and active: {} ",
            userDetails.getUsername(),
            isTokenValid,
            isTokenActive);

        if (isTokenValid && isTokenActive) {
          UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());

          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }

      filterChain.doFilter(request, response);
    } catch (Exception exception) {
      handlerExceptionResolver.resolveException(request, response, null, exception);
    }
  }
}
