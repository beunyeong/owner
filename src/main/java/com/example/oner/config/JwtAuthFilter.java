package com.example.oner.config;

import com.example.oner.util.AuthenticationScheme;
import com.example.oner.util.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * create on 2024. 12. 21. create by IntelliJ IDEA.
 *
 * <p> request마다 처리하는 JWT 기반 인증 필터. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j(topic = "Security::JwtAuthFilter")
public class JwtAuthFilter extends OncePerRequestFilter {

  /**
   * JWT 토큰 제공자.
   */
  private final JwtProvider jwtProvider;

  /**
   * UserDetailsService.
   */
  private final UserDetailsService userDetailsService;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info("URI: {}", request.getRequestURI());
    this.authenticate(request);
    filterChain.doFilter(request, response);
  }

  /**
   * request를 이용해 인증을 처리한다.
   *
   * @param request {@link HttpServletRequest}
   */
  private void authenticate(HttpServletRequest request) {
    log.info("인증 처리.");

    // 토큰 검증.
    String token = this.getTokenFromRequest(request);
    if (!jwtProvider.validToken(token)) {
      return;
    }

    // 토큰으로부텨 username을 추출.
    String username = this.jwtProvider.getUsername(token);

    // username에 해당되는 사용자를 찾는다.
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    // SecurityContext에 인증 객체 저장.
    this.setAuthentication(request, userDetails);
  }

  /**
   * request의 Authorization 헤더에서 토큰 값을 추출.
   *
   * @param request {@link HttpServletRequest}
   * @return 토큰 값 (찾지 못한 경우 {@code null})
   */
  private String getTokenFromRequest(HttpServletRequest request) {
    final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String headerPrefix = AuthenticationScheme.generateType(AuthenticationScheme.BEARER);

    boolean tokenFound =
        StringUtils.hasText(bearerToken) && bearerToken.startsWith(headerPrefix);
    if (tokenFound) {
      return bearerToken.substring(headerPrefix.length());
    }

    return null;
  }

  /**
   * {@code SecurityContext}에 인증 객체를 저장한다.
   *
   * @param request     {@link HttpServletRequest}
   * @param userDetails 찾아온 사용자 정보
   */
  private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
    log.info("SecurityContext에 Authentication 저장.");

    // 찾아온 사용자 정보로 인증 객체를 생성.
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    // SecurityContext에 인증 객체 저장.
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
