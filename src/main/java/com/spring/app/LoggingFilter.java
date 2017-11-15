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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.spring.app.service.OauthTokenService;

@Component
public class LoggingFilter implements Filter {

	private ConnectionRepository connectionRepository;

	private Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

	@Autowired
	private OauthTokenService oauthTokenService;

	@Value("${spring.social.twitter.app-id}")
	private String appId;

	@Value("${spring.social.twitter.app-secret}")
	private String appSecret;

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
		Cookie cookies[] =httpRequest.getCookies();
		String nextUrl = httpRequest.getRequestURI();
		//アクセストークンによりログインをする
		if("/twitter".equals(nextUrl) && connectionRepository.findPrimaryConnection(Twitter.class) == null && cookies !=null){
			for (Cookie cookie : cookies ) {
				if ("accessToken".equals(cookie.getName())) {
					List<OauthToken> oauthToken = oauthTokenService.findByAccessToken(cookie.getValue());
					if(!oauthToken.isEmpty()){
						cookie = this.cookieCreate(oauthToken.get(0).getOAuthToken().getValue());
						httpResponse.addCookie(cookie);
						TwitterConnectionFactory connectionFactory = new TwitterConnectionFactory(appId,appSecret);
						Connection<Twitter> connection = connectionFactory.createConnection(oauthToken.get(0).getOAuthToken());
						connectionRepository.addConnection(connection);
						logger.info("ログイン");
					}
				}
			}
		}
		if(nextUrl.contains("api/")) {
			boolean checkCookie = true;
			if(cookies !=null){
				for (Cookie cookie : cookies ) {
					if ("accessToken".equals(cookie.getName())) {
						List<OauthToken> oauthToken = oauthTokenService.findByAccessToken(cookie.getValue());
						if(!oauthToken.isEmpty()){
							checkCookie=false;
						}
					}
				}
			}
			if(checkCookie){
				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"アクセストークンが認証されませんでした");
			}
		}
		if(!StringUtils.isEmpty(request.getParameter("oauth_token"))){
			String oauthToken = request.getParameter("oauth_token");
			String oauthVerifier = request.getParameter("oauth_verifier");
			TwitterConnectionFactory connectionFactory = new TwitterConnectionFactory(appId,appSecret);
			//token認証でtwitterログイン
			OAuthToken accessToken = accessToken(oauthToken,oauthVerifier);
			Connection<Twitter> connection = connectionFactory.createConnection(accessToken);
			Cookie cookie = this.cookieCreate(accessToken.getValue());
			httpResponse.addCookie(cookie);
			connectionRepository.addConnection(connection);
		}
		chain.doFilter(request, response);
		logger.info("After!!");
	}

	public OAuthToken accessToken(String oauth_token,String oauth_verifier){
		TwitterConnectionFactory connectionFactory = new TwitterConnectionFactory(appId,appSecret);
		OAuth1Operations oauthOperations = connectionFactory.getOAuthOperations();
		OAuthToken requestToken = new OAuthToken(oauth_token,oauth_verifier);
		AuthorizedRequestToken authorizedRequestToken = new AuthorizedRequestToken(requestToken, oauth_verifier);
		OAuthToken accessToken= oauthOperations.exchangeForAccessToken(authorizedRequestToken, null);
		//tokenをDBに登録する
		OauthToken token = new OauthToken();
		token.setAccessToken(accessToken.getValue());
		token.setAccessVerifier(accessToken.getSecret());
		token.setOAuthToken(accessToken);
		List<OauthToken> oauthTokens = oauthTokenService.findByAccessToken(accessToken.getValue());
		//DBによって処理変更
		if(oauthTokens.isEmpty()){
			oauthTokenService.create(token);
		}
		return accessToken;
	}

	public Cookie cookieCreate(String token){
		Cookie cookie = new Cookie("accessToken",token);
		cookie.setMaxAge(60 * 30);
		cookie.setPath("/");
		cookie.setSecure(false);
		return cookie;
	}

	@Override
	public void destroy() {
		logger.info("destroy!!!");
	}
}
