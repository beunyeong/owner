package com.example.oner.config.auth;

import com.example.oner.entity.User;
import com.example.oner.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * create on 2024. 12. 21. create by IntelliJ IDEA.
 *
 * <p> {@link UserDetails}의 구현체 클래스. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
@Slf4j(topic = "Security::UserDetailsImpl")
public class UserDetailsImpl implements UserDetails {

  /**
   * User entity.
   */
  private final User user;

  /**
   * 계정의 권한 리스트를 리턴.
   *
   * @return {@code Collection<? extends GrantedAuthority>}
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    UserRole role = this.user.getUserRole();
    log.info("사용자 권한: {}", role.getAuthorities());

    return new ArrayList<>(role.getAuthorities());
  }

  /**
   * 사용자의 자격 증명 반환.
   *
   * @return 암호
   */
  @Override
  public String getPassword() {
    return this.user.getPassword();
  }

  /**
   * 사용자의 자격 증명 반환.
   *
   * @return 사용자 이름
   */
  @Override
  public String getUsername() {
    return this.user.getEmail();
  }

  /**
   * 계정 만료.
   *
   * @return 사용 여부
   * @apiNote 사용하지 않을 경우 true를 리턴하도록 재정의.
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * 계정 잠금.
   *
   * @return 사용 여부
   * @apiNote 사용하지 않을 경우 true를 리턴하도록 재정의.
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * 자격 증명 만료.
   *
   * @return 사용 여부
   * @apiNote 사용하지 않을 경우 true를 리턴하도록 재정의.
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * 계정 활성화.
   *
   * @return 사용 여부
   * @apiNote 사용할 경우 true를 리턴하도록 재정의.
   */
  @Override
  public boolean isEnabled() {
    return true;
  }
}
