package com.example.oner.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * create on 2024. 12. 21. create by IntelliJ IDEA.
 *
 * <p> 인증 방식. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 * @see <a href="https://developer.mozilla.org/ko/docs/Web/HTTP/Authentication#%EC%9D%B8%EC%A6%9D_%EC%8A%A4%ED%82%A4%EB%A7%88">인증 스키마</a>
 */
@Getter
@RequiredArgsConstructor
public enum AuthenticationScheme {
  BEARER("Bearer");

  private final String name;

  /**
   * Authorization 헤더의 값으로 사용될 prefix를 생성.
   *
   * @param authenticationScheme {@link AuthenticationScheme}
   * @return 생성된 prefix
   */
  public static String generateType(AuthenticationScheme authenticationScheme) {
    return authenticationScheme.getName() + " ";
  }
}
