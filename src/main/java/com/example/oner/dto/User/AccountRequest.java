package com.example.oner.dto.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * create on 2024. 12. 21. create by IntelliJ IDEA.
 *
 * <p> 로그인 request를 위한 DTO. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Getter
public class AccountRequest {

  /**
   * 이메일 (Spring Security에서 username으로 식별하기 위함).
   */
  @NotBlank(message = "email은 빈 값이 허용되지 않습니다.")
  @Email(message = "올바른 email 형식이 아닙니다.")
  private final String email;

  /**
   * 암호.
   */
  @NotBlank(message = "password는 빈 값이 허용되지 않습니다.")
  private final String password;

  /**
   * 생성자.
   *
   * @param email    이메일
   * @param password 암호
   */
  public AccountRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
