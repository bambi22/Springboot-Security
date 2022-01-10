package com.cos.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;

@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록됨.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //시큐어 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired private PrincipalOauth2UserService principalOauth2UserService;
	
	// 1. 코드 받기(인증) 2. 액세스 토큰(권한) 3. 사용자 정보 가져오기 4-1. 정보를 토대로 회원가입 자동 시키거나 4-2. 추가 정보 필요 시 입력 후 회원가입
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated() // 인증만 되면 들어갈 수 있는 주소
			.antMatchers("/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/admin/**").access(" hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
		.and()
			.formLogin()
			.loginPage("/loginForm")
		    .loginProcessingUrl("/login")		// /login 호출되면 시큐리티가 대신 로그인 진행
		    .defaultSuccessUrl("/")
		    .and()
		    .oauth2Login()
		    .loginPage("/loginForm")
		    .userInfoEndpoint()
		    .userService(principalOauth2UserService); // 구글 로그인 완료 후 후처리 필요 Tip. 코드X, (액세스토큰 + 프로필 정보 O)
	}
}
