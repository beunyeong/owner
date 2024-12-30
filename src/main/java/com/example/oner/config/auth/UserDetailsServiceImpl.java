package com.example.oner.config.auth;

import com.example.oner.entity.User;
import com.example.oner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * create on 2024. 12. 21. create by IntelliJ IDEA.
 *
 * <p> {@link UserDetailsService}의 구현체 클래스. </p>
 *
 * @author Seokgyu Hwang (Chris)
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "Security::UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

  /**
   * User entity의 repository.
   */
  private final UserRepository userRepository;

  /**
   * 입력받은 이메일에 해당하는 사용자 정보를 찾아 리턴.
   *
   * @param username username
   * @return 해당하는 사용자의 {@link UserDetailsImpl} 객체
   * @throws UsernameNotFoundException 이메일에 해당하는 사용자를 찾지 못한 경우
   * @apiNote 이 애플리케이션에서는 사용자의 이메일을 {@code username}으로 사용합니다
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = this.userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    log.info("찾은 사용자: {}", username);
    return new UserDetailsImpl(user);
  }
}
