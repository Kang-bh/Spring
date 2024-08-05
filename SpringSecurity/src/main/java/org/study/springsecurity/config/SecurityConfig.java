package org.study.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Spring Boot에게 Configuration으로 등록 필요
@EnableWebSecurity // Security 설정을 위해. 이를 통해 해당 클래스가 Spring Security에서도 관리
public class SecurityConfig {

    // Bcycrpt 암호화
    @Bean // 어디서든 사용가능하도록 (회원가입 etc)
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder(); // 암호화 진행할 Encoder 생성자 함수
    }


    // 특정 메소드 통해 Bean 주입

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception { // SecurityFilterChain interface 이용

        // 특정 경로 요청 시 해당 경로 모든 사용자에게 오픈
        // 특정 admin 경로는 admin권한 가진 사용자에게 오픈

        // 해당 경로들에 대한 특정 권한 설정
        http.authorizeHttpRequests((auth) -> auth // 해당 방식 통해 권한 확인, 작성은 lambda식으로 작성
                        .requestMatchers("/", "/login", "/join", "joinProc").permitAll() // root 경로 및 login 경로에 대한 특정 권한 부여 , hasRole, permitAll, authenticated(로그인만 진행하면)
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER") // 여러 유저 아이디 직접 설정하기 어렵기에 와일드 카드 이용
                        .anyRequest().authenticated() // 처리하지 못할 어떤 경로 모든지
                // denyAll 모든 사용자
                // 상단부터 처리된다.
        );

        // csrf 사이트 위변조 방지 동작되면 post 요청 시에 csrf 토큰도 보내줘야 로그인이 진행된다. but 개발 환경에서는
        // http
        // .csrf((auth) -> auth.disable()); // 추후 enable

        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true));

        http
                // custom한 loginPage경로 설정
                .formLogin((auth) -> auth.loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .permitAll()
                ); // Spring Security가 자동으로 해당 경로를 통해 받아서 로그인 처리 진행

        // 세션 고종 보호
        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId()); // 세션은 동일하지만 세션 쿠키 id값을 변경 시켜 해커와 유저가 가지는 id을 다르게 설정

        return http.build();
    }


}
