package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;

// JpaRepository 상속했기 때문에 어노테이션 필요X
public interface UserRepository extends JpaRepository<User, Integer>{
	public User findByUsername(String username); //JPA Query methods
	
}
