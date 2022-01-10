package com.cos.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.security1.model.User;

import lombok.Data;

// 시큐리티가 로그인 낚아채서 로그인 진행
// 로그인 완료되면 시큐리티 session을 만들어줌.
// Authentication 타입 객체 안 User 정보
// User 오브젝트 타입 -> UserDetails 타입 객체

@Data
public class PrincipalDetails implements UserDetails, OAuth2User{
	private User user; //콤포지션
	private Map<String, Object> attributes;
	
	// 일반 로그인 생성자
	public PrincipalDetails(User user) {
		this.user = user;
	}
	// oauth 로그인 생성자
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		// 사이트 1년 동안 회원 로그인 안한다면.
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		//return (String) attributes.get("sub");
		return null; // 사용 안해서 null
	}

}
