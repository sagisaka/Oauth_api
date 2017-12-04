package com.spring.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.app.TimeValidater;
import com.spring.app.model.OauthToken;
import com.spring.app.repository.OauthTokenRepository;

@Service
@Transactional
public class OauthTokenService {

	@Autowired
	private OauthTokenRepository repository;

	private TimeValidater timeValidater = new TimeValidater();

	public List<OauthToken> findAll(){
		return repository.findAll();
	}

	public OauthToken create(OauthToken oauthToken){
		return repository.save(oauthToken);
	}

	public List<OauthToken> findByAccessToken(String accessToken) {
		return repository.findByAccessToken(accessToken);
	}	
	public List<OauthToken> findByApiAccessToken(String apiAccessToken) {
		return repository.findByApiAccessToken(apiAccessToken);
	}

	public OauthToken findOne(Integer id){
		return repository.findOne(id);
	}

	public void delete(Integer id){
		repository.delete(id);
	}

	public OauthToken update(OauthToken oauthToken, OauthToken anotherOauthToken) {
		oauthToken.setAccessToken(anotherOauthToken.getAccessToken());
		oauthToken.setAccessVerifier(anotherOauthToken.getAccessVerifier());
		oauthToken.setOAuthToken(anotherOauthToken.getOAuthToken());
		return repository.save(oauthToken);
	}

	public List<OauthToken> findTokenExpiration() {
		List<OauthToken> tokens = repository.findAll();
		List<OauthToken> expirationToken = new ArrayList<OauthToken>();
		for(OauthToken token :tokens){
			if(timeValidater.isPeriodValidation(token.getTokenExpiration())){
				expirationToken.add(token);
			}
		}
		return expirationToken;
	}
}
