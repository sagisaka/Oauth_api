package com.spring.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.app.model.OauthToken;
import com.spring.app.repository.OauthTokenRepository;

@Service
@Transactional
public class OauthTokenService {

	@Autowired
	private OauthTokenRepository repository;

	public List<OauthToken> findAll(){
		return repository.findAll();
	}

	public OauthToken create(OauthToken oauthToken){
		return repository.save(oauthToken);
	}

	public List<OauthToken> find(String accessToken) {
		return repository.findByAccessToken(accessToken);
	}	

	public OauthToken findOne(Integer id){
		return repository.findOne(id);
	}

	public void delete(Integer id){
		repository.delete(id);
	}

	public OauthToken update(OauthToken anotherOauthToken) {
		OauthToken oauthToken = this.findOne(1);
		oauthToken.setAccessToken(anotherOauthToken.getAccessToken());
		oauthToken.setAccessVerifier(anotherOauthToken.getAccessVerifier());
		oauthToken.setOAuthToken(anotherOauthToken.getOAuthToken());
		oauthToken.setCheckLogin(true);
		return repository.save(oauthToken);
	}
	public OauthToken updateCheck(Boolean check) {
		OauthToken oauthToken = this.findOne(1);
		oauthToken.setCheckLogin(check);
		return repository.save(oauthToken);
	}
	public List<OauthToken> checkLogin(Boolean check) {
		return repository.findByCheckLogin(check);
	} 
}
