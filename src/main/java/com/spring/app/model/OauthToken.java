package com.spring.app.model;

import lombok.Data;

@Data
public class OauthToken {
	String oauth_token;
	String oauth_verifier;
}
