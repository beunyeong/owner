package com.example.oner.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;

/**
 * create on 2024. 12. 21. create by IntelliJ IDEA.
 *
 * <p> Response body에 담을 공통 객체. </p>
 * <p> 모든 API를 통일된 형태로 응답하기 위해 생성했습니다. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Getter
public class CommonResponseBody<T> {

  /**
   * Response 메세지.
   */
  private final String message;

  /**
   * Response 데이터.
   */
  @JsonInclude(Include.NON_NULL)
  private final T data;

  /**
   * 생성자.
   *
   * @param message Response 메세지
   * @param data    Response 데이터
   */
  public CommonResponseBody(String message, T data) {
    this.message = message;
    this.data = data;
  }

  /**
   * 생성자. {@code data} 필드는 {@code null}로 초기화 합니다.
   *
   * @param message Response 메세지
   */
  public CommonResponseBody(String message) {
    this(message, null);
  }
}
