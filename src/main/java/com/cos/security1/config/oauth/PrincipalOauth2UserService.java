package com.cos.security1.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{

	@Autowired private UserRepository userRepository;
	@Autowired BCryptPasswordEncoder bCryptPasswordEncoder;
	
	// 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
	//함수 종료시 @AuthenticationPrincipal 어노테이션 만들어진다.
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		//System.out.println("userRequest : " + userRequest);
		//System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); // registarationId로 어떤 OAuth로 로그인했는지 알 수 있음
		//System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		// 구글 로그인 완료 -> code를 리턴 (OAuth-Client라이브러리)-> AccessToken 요청
		// userRequest 정보 -> loadUser함수 호출 -> 회원 프로필 받아줌
		//System.out.println("getAttributes : " +super.loadUser(userRequest).getAttributes());
		
		// 회원가입 진행
		String provider = userRequest.getClientRegistration().getClientId();
		String providerId = oauth2User.getAttribute("sub");
		String username = provider +"_" + providerId;
		String password = bCryptPasswordEncoder.encode("밤비구글");
		String email = oauth2User.getAttribute("email");
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		if(userEntity == null) {
			userEntity = User.builder().username(username).password(password).email(email).role(role).provider(providerId).providerId(providerId).build();
			userRepository.save(userEntity);
		}
		
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
	}
}
