package com.spring.app;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

	private Twitter twitter;

	private Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

	private TimeValidater timeValidater = new TimeValidater();

	@Autowired
	private OauthTokenService oauthTokenService;

	@Value("${spring.social.twitter.app-id}")
	private String appId;

	@Value("${spring.social.twitter.app-secret}")
	private String appSecret;

	@Inject
	public LoggingFilter(ConnectionRepository connectionRepository,Twitter twitter) {
		this.connectionRepository = connectionRepository;
		this.twitter = twitter;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("init!!");
		//定期ジョブ実行
		//jobPeriodical.oneDayJob(logProductService);
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("Before!!");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String nextUrl = httpRequest.getRequestURI();
		//	アクセストークンによりログインをする
		if("/twitter".equals(nextUrl) && connectionRepository.findPrimaryConnection(Twitter.class) == null){
			List<OauthToken> oauthToken = oauthTokenService.findByApiAccessToken(httpRequest.getHeader("Authorization"));
			if(!oauthToken.isEmpty()){
				if (timeValidater.isPeriodValidation(oauthToken.get(0).getTokenExpiration())) {
					TwitterConnectionFactory connectionFactory = new TwitterConnectionFactory(appId,appSecret);
					Connection<Twitter> connection = connectionFactory.createConnection(oauthToken.get(0).getOAuthToken());
					connectionRepository.addConnection(connection);
					logger.info("ログイン");
				}
			}
		}
		//アクセストークン認証
		if(nextUrl.contains("api/")) {
			if(!this.apiValidation(httpRequest)){
				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"アクセストークンが認証されませんでした");
			}
		}
		//oauthTokenをDBに格納
		if(!StringUtils.isEmpty(request.getParameter("oauth_token"))){
			String oauthToken = request.getParameter("oauth_token");
			String oauthVerifier = request.getParameter("oauth_verifier");
			TwitterConnectionFactory connectionFactory = new TwitterConnectionFactory(appId,appSecret);
			//token認証でtwitterログイン
			OAuthToken accessToken = accessToken(oauthToken,oauthVerifier);
			Connection<Twitter> connection = connectionFactory.createConnection(accessToken);
			connectionRepository.addConnection(connection);
			//tokenをDBに登録する
			OauthToken token = new OauthToken();
			token.setAccessToken(accessToken.getValue());
			token.setAccessVerifier(accessToken.getSecret());
			token.setOAuthToken(accessToken);
			token.setAuthor(twitter.userOperations().getUserProfile().getName());
			//現在時刻を表示
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			// 日時を加算する
			calendar.add(Calendar.MINUTE, 60);
			token.setTokenExpiration(calendar);
			String apiAccessToken = UUID.randomUUID().toString();
			token.setApiAccessToken(apiAccessToken);
			//apiAccessTokenを出力
			logger.info(apiAccessToken);
			oauthTokenService.create(token);
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
		return accessToken;
	}
	public boolean apiValidation(HttpServletRequest httpRequest){
		List<OauthToken> oauthToken = oauthTokenService.findByApiAccessToken(httpRequest.getHeader("Authorization"));
		if(!oauthToken.isEmpty()){
			if (timeValidater.isPeriodValidation(oauthToken.get(0).getTokenExpiration())) {
				return true;
			}
		}
		return false;		
	}

	@Override
	public void destroy() {
		logger.info("destroy!!!");
	}
}
