package com.koreait.cloneinstagram.security.model;

import com.koreait.cloneinstagram.user.model.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomUserPrincipal implements OAuth2User, UserDetails {//로컬, 소셜 로그인을 같은 객체로 사용하기 위함, 분리하면 빡세짐
    @Getter private UserEntity user;
    private Map<String, Object> attributes;

    public CustomUserPrincipal(UserEntity user) {
        this.user = user;
    }

    //객체화 하지 않고 바로 호출
    public static CustomUserPrincipal create(UserEntity user) {
        return new CustomUserPrincipal(user);
    }

    public static CustomUserPrincipal create(UserEntity user, Map<String, Object> attributes) {
        CustomUserPrincipal userPrincipal = create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    //비밀번호
    @Override
    public String getPassword() {
        return user.getPw();
    }

    //아이디(이메일)
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    //전부 true 여야 진행
    //계정 기간만료 여부(하드)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 락 걸려있는지 여부(하드)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //신용(하드)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    //하드코딩, 항상 ROLE_USER 가 리턴 되도록
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getName() {
        return user.getNm();
    }
}
