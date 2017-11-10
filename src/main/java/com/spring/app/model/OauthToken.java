package com.spring.app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.social.oauth1.OAuthToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="oauthToken")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OauthToken {

	@Id
	@GeneratedValue
	private Integer id;

	private String accessToken;

	private String accessVerifier;

	private OAuthToken oAuthToken;
}
