package com.spring.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.app.model.OauthToken;

public interface OauthTokenRepository
extends JpaRepository<OauthToken, Integer> {
	List<OauthToken> findByOauthToken(String find);

}
