package com.koreait.cloneinstagram.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor//Autowried 안써도 되는 어노테이션, final 만 추가해주면 된다, 소스의 구조가 이상하면 구동도 안하고, 어디가 문제인지 알려준다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOauth2UserService;
    private final CustomUserDetailsService customUserDetailsService;

    //메소드에 Bean 등록, UserService 에서
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }//암호 BCrypt 사용, 쓰고하자하는 암호화 사용하려면 여기만 바꿔주면 된다. 하지만 BCrpi t 가 좋음

    //홈 로그인 셋팅, 로컬 (비밀번호를 암호화)
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); //ajax-post 막힘을 풀어주기 위해(전체다 풀어줌), 이거 풀면 보안약해짐(오픈 API 쓰려면 풀어야함)

        http
            .authorizeRequests()
                .antMatchers("/css/**", "/img/**", "/js/**", "/user/signin", "/user/signup").permitAll()//매칭되는건 로그인 안해도 들어갈수 있게, 로그인 안해도
                .anyRequest().authenticated();//로그인하면 모든거 가능

        //로컬로그인을 위한 코드(셋팅)
        http.formLogin()
                .loginPage("/user/signin")//페이지 설정 부분
                .usernameParameter("email")//uid 부분을 email 로 쓸거라는 설정, 이름이 같다면 굳이 세팅할 필요 없음
                .passwordParameter("pw")//마찬가지
                .defaultSuccessUrl("/feed", true);//성공하면 어디로 리다이렉트

        //소셜로그인을 위한 코드(셋팅)
        http.oauth2Login()
                .loginPage("/user/signin")
                .defaultSuccessUrl("/feed", true)
                .failureUrl("/user/signin")
                .userInfoEndpoint() //OAuth 2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당합니다.
                .userService(customOauth2UserService);

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/signout"))
                .logoutSuccessUrl("/user/signin")
                .invalidateHttpSession(true);
    }


}
