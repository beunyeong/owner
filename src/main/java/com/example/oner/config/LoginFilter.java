package com.example.oner.config;

import com.example.oner.dto.User.LoginResponseDto;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginFilter implements Filter {

    // 화이트 리스트 : 로그인 없이 접근 가능한 URL 목록
    private static final String[] WHITE_LIST = {"/users/signup","/users/login"};

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {

        // HttpServletRequest로 형변환하여 HTTP 요청 정보 사용
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURL = httpServletRequest.getRequestURI(); // 요청된 URL 추출

        // HttpServletResponse로 형변환하여 HTTP 응답 제어
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // 요청 URL이 화이트리스트에 포함되지 않은 경우 처리
        if (!isWhiteList(requestURL)) {
            // 세션을 가져옴. 세션이 존재하지 않으면 null 반환
            HttpSession session = httpServletRequest.getSession(false);

            // 세션이 없거나 로그인 정보가 없는 경우
            if (session == null || session.getAttribute(Const.LOGIN_USER) == null) {
                log.warn("Unauthorized access attempt to {}", requestURL); // 로그 경고 출력
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 설정
                httpServletResponse.getWriter().write("로그인 해주세요"); // 클라이언트에 메시지 전송
                return;
            }

            // 세션에 저장된 객체가 LoginResponseDto인지 확인
            LoginResponseDto loginUser = (LoginResponseDto) session.getAttribute(Const.LOGIN_USER);
            if (loginUser == null) {
                log.warn("Invalid session data for {}", requestURL); // 잘못된 세션 데이터 로그 출력
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 상태 설정
                httpServletResponse.getWriter().write("로그인 해주세요"); // 클라이언트에 메시지 전송
                return;
            }
            log.info("success login");
        }
        // 필터 체인에 현재 요청과 응답 전달
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 요청 URL이 화이트리스트에 포함되어 있는지 확인
     *
     * @param requestURL 요청된 URL
     * @return 화이트리스트에 포함되어 있으면 true, 아니면 false
     */
    private boolean isWhiteList(String requestURL) {
        // 화이트리스트와 요청 URL을 단순 매칭
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURL);
    }
}
