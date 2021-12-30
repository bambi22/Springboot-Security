package com.cos.security1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Service
public class UserService {

	@Autowired BCryptPasswordEncoder encoder;
	@Autowired UserRepository userRepository;

	public void join(User user) {
		user.setRole("ROLE_USER");
		String rawPw = user.getPassword();
		String encPw = encoder.encode(rawPw);
		user.setPassword(encPw);
		userRepository.save(user);
	}

}
