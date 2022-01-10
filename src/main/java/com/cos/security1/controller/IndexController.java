package com.cos.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.service.UserService;

@Controller
public class IndexController {
	@Autowired UserService userService;
	// /login/oauth2/code 고정
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); 
		System.out.println("authentication : " + principalDetails.getUser());
		System.out.println("UserDetails : " + userDetails.getUser()); // 일반 로그인
		return "세션 정보 확인하기";
	}
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOauthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) { // PrincipalDetails, UserDetails 다운 캐스팅 에러 
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal(); 
		System.out.println("authentication : " + oauth2User.getAttributes());
		System.out.println("oAuth2User : " + oauth.getAttributes()); // oauth 로그인
		return "OAuth 세션 정보 확인하기";
	}
	
	@GetMapping({ "", "/" })
	public @ResponseBody String index() {
		return "인덱스 페이지입니다.";
	}
	
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) { // OAuth 로그인, 일반 로그인을 해도 PrincipalDetails 가능
		System.out.println("PrincipalDetails : " + principalDetails.getUser());
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() throws Exception {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() throws Exception {
		return "manager";
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		userService.join(user);
		return "redirect:loginForm";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터 조회";
	}
}
