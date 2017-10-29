package com.spring.app.model;

import lombok.Data;

@Data
public class OauthToken {
	private String oauth_token;
	private String oauth_verifier;
}
