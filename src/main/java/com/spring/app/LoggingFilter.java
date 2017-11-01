package com.spring.app;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.spring.app.model.OauthToken;
import com.spring.app.repository.OauthTokenRepository;

@Component
public class LoggingFilter implements Filter {

	private ConnectionRepository connectionRepository;

	private Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

	@Autowired
	private OauthTokenRepository repository;
	
	private OauthToken token = new OauthToken();
	
	private String appId = "PgJdaamNXGzzKYWf5zEgdNmzN";
	private String appSecret = "tC7soU8JLmh72qpjLZJ2GbcpCC1Eek3lRp7mt3yRCBZyDAPSIL";
	
	@Inject
	public LoggingFilter(ConnectionRepository connectionRepository) {
		this.connectionRepository = connectionRepository;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("init!!");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("Before!!");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String nextUrl = httpRequest.getRequestURI();
		if (isLoginCheckPage(nextUrl)){
			((HttpServletResponse)response).sendRedirect("/connect/twitter");
		}
		if(!StringUtils.isEmpty(request.getParameter("oauth_token"))){
			String oauthToken = request.getParameter("oauth_token");
			String oauthVerifier = request.getParameter("oauth_verifier");
			TwitterConnectionFactory connectionFactory = new TwitterConnectionFactory(appId,appSecret);
			//token認証でtwitterログイン
			Connection<Twitter> connection = connectionFactory.createConnection(accessToken(oauthToken,oauthVerifier));
			connectionRepository.addConnection(connection);
		}
//		if(nextUrl.contains("api/") && connectionRepository.findPrimaryConnection(Twitter.class) == null) {
//			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"アクセストークンが認証されませんでした");
//			return;
//		}
		chain.doFilter(request, response);
		logger.info("After!!");
	}

	public OAuthToken accessToken(String oauth_token,String oauth_verifier){
		TwitterConnectionFactory connectionFactory = new TwitterConnectionFactory(appId,appSecret);
		OAuth1Operations oauthOperations = connectionFactory.getOAuthOperations();
		OAuthToken requestToken = new OAuthToken(oauth_token,oauth_verifier);
		AuthorizedRequestToken authorizedRequestToken = new AuthorizedRequestToken(requestToken, oauth_verifier);
		OAuthToken accessToken= oauthOperations.exchangeForAccessToken(authorizedRequestToken, null);
		token.setOauthToken(accessToken.getValue());
		token.setOauthVerifier(accessToken.getSecret());
		List<OauthToken> oauthTokens =repository.findByOauthToken(accessToken.getValue());
		if(oauthTokens.isEmpty()) repository.save(token);
		return accessToken;
	}
	
	private boolean isLoginCheckPath(String requestUri) {
		if(requestUri.contains("js/") || requestUri.contains("css/") || requestUri.contains("image/")){
			return true;
		}
		return false;
	}

	private boolean isLoginCheckPage(String nextUrl){
		if (!(isLoginCheckPath(nextUrl))
				&& connectionRepository.findPrimaryConnection(Twitter.class) == null
				&& !("/connect/twitter".equals(nextUrl))){
			return true;
		}
		return false;
	}

	@Override
	public void destroy() {
		logger.info("destroy!!!");
	}
}
