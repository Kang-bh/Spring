package com.filespace.domain.user.jwt;

import com.filespace.domain.user.dto.LoginResponseDTO;
import com.filespace.domain.user.dto.ResultDTO;
import com.filespace.domain.user.domain.Refresh;
import com.filespace.domain.user.domain.User;
import com.filespace.domain.user.repository.RefreshRepository;
import com.filespace.domain.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private RefreshRepository refreshRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshRepository refreshRepository, UserRepository userRepository){
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String email = obtainEmail(request);
        String password = obtainPassword(request);


        System.out.println(email);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email,password,null);
        return authenticationManager.authenticate(authToken);
    }
    protected String obtainEmail(HttpServletRequest request){
        return request.getParameter("email");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        request.setCharacterEncoding("UTF-8");
        String email = authentication.getName();
        User data = userRepository.findByEmail(email);

        LoginResponseDTO loginResponseDTO = convertToLoginResponseDTO(data);
        System.out.println(loginResponseDTO);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //role
        //토큰 생성
        Long expiredMs = 1000 * 60 * 24 * 60L;
        String access = jwtUtil.createJwt("access", email, expiredMs);
        String refresh = jwtUtil.createJwt("refresh", email, expiredMs);

        addRefreshEntity(email, refresh, 86400000L);
        Cookie cookie = createCookie("refresh", refresh);
        String cookieHeader = String.format("%s=%s; Max-Age=%d; Path=%s; Secure; HttpOnly; SameSite=None",
                cookie.getName(), cookie.getValue(), cookie.getMaxAge(),
                cookie.getPath() != null ? cookie.getPath() : "/");
        //응답 설정
        response.setHeader("access", access);
        response.addHeader("Set-Cookie", cookieHeader);
        response.addCookie(cookie);
        response.setStatus(HttpStatus.OK.value());
        loginResponseDTO.setAccess(access);
        response.getWriter().write(new ResultDTO<>("success", "Login success",loginResponseDTO).toJson());

    }
    private LoginResponseDTO convertToLoginResponseDTO(User user) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setId(user.getId());
        loginResponseDTO.setEmail(user.getEmail());
        loginResponseDTO.setNickname(user.getNickname());
        System.out.println(user.getNickname());
        loginResponseDTO.setProfile_image(user.getProfile_image());

        // 기타 필드 설정

        return loginResponseDTO;
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(401);
        response.getWriter().write(new ResultDTO<>("error", "Login fail. invalid email or password").toJson());
    }
    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refreshEntity = new Refresh();
        refreshEntity.setEmail(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
