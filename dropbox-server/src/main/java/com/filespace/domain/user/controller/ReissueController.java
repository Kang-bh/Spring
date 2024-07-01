package com.filespace.domain.user.controller;

import com.filespace.domain.user.dto.LoginResponseDTO;
import com.filespace.domain.user.dto.ResultDTO;
import com.filespace.domain.user.domain.User;
import com.filespace.domain.user.jwt.JWTUtil;
import com.filespace.domain.user.repository.RefreshRepository;
import com.filespace.domain.user.domain.Refresh;
import com.filespace.domain.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;

@Controller
@ResponseBody
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final UserRepository userRepository;

    public ReissueController(JWTUtil jwtUtil, RefreshRepository refreshRepository, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
        this.userRepository = userRepository;
    }

    @Tag(name = "토큰 재발급", description = "access토큰이 만료되면 재발급 받는 페이지입니다.")
    @PostMapping("/reissue")
    public void reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {

            //response status code
            response.getWriter().write(new ResultDTO<>("fail", "refresh token null", null).toJson());
            //return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            response.getWriter().write(new ResultDTO<>("fail", "expired refresh token", null).toJson());

            //response status code
            //return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            //response status code

            response.getWriter().write(new ResultDTO<>("fail", "invalid refresh token", null).toJson());
            //return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if(!isExist) {
            response.getWriter().write(new ResultDTO<>("fail", "invalid refresh token", null).toJson());
            //return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String email = jwtUtil.getEmail(refresh);
        User data = userRepository.findByEmail(email);
        LoginResponseDTO loginResponseDTO = convertToLoginResponseDTO(data);
        //String role = jwtUtil.getRole(refresh);

        //role
        //make new JWT
        String newAccess = jwtUtil.createJwt("access", email, 600000L);

        //role
        String newRefresh = jwtUtil.createJwt("refresh", email, 86400000L);

        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(email, newRefresh, 86400000L);

        Cookie cookie = createCookie("refresh", refresh);
        String cookieHeader = String.format("%s=%s; Max-Age=%d; Path=%s; Secure; HttpOnly; SameSite=None",
                cookie.getName(), cookie.getValue(), cookie.getMaxAge(),
                cookie.getPath() != null ? cookie.getPath() : "/");
        //응답 설정
        response.setHeader("access", newAccess);
        response.addHeader("Set-Cookie", cookieHeader);
        response.addCookie(cookie);
        //response
        //response.setHeader("access", newAccess);
        response.setStatus(HttpStatus.OK.value());

        response.addCookie(createCookie("refresh", newRefresh));
        loginResponseDTO.setAccess(newAccess);

        response.getWriter().write(new ResultDTO<>("success", "reissue access token", loginResponseDTO).toJson());
        //return new ResponseEntity<>("access token 재발급 성공", HttpStatus.OK);


    }
    private LoginResponseDTO convertToLoginResponseDTO(User userEntity) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setId(userEntity.getId());
        loginResponseDTO.setEmail(userEntity.getEmail());
        loginResponseDTO.setNickname(userEntity.getNickname());
        System.out.println(userEntity.getNickname());
        loginResponseDTO.setProfile_image(userEntity.getProfile_image());

        // 기타 필드 설정

        return loginResponseDTO;
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


