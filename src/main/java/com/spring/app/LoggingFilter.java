package com.spring.app;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.spring.app.model.OauthToken;

@Component
public class LoggingFilter implements Filter {

	private ConnectionRepository connectionRepository;
	private Twitter twitter;
	
	OauthToken token = new OauthToken();
	@Inject
	public LoggingFilter(Twitter twitter,ConnectionRepository connectionRepository) {
		this.twitter = twitter;
		this.connectionRepository = connectionRepository;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("init!!");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("Before!!");
		HttpServletRequest hreq = (HttpServletRequest) request;
		String nextUrl = hreq.getRequestURI();
		if (isLoginCheckPage(nextUrl)){
			((HttpServletResponse)response).sendRedirect("/connect/twitter");
		}
		if(!StringUtils.isEmpty(request.getParameter("oauth_token"))){
			String oauth_token = request.getParameter("oauth_token");
			String oauth_verifier = request.getParameter("oauth_verifier");
			TwitterConnectionFactory connectionFactory = new TwitterConnectionFactory("PgJdaamNXGzzKYWf5zEgdNmzN","tC7soU8JLmh72qpjLZJ2GbcpCC1Eek3lRp7mt3yRCBZyDAPSIL");
			OAuth1Operations oauthOperations = connectionFactory.getOAuthOperations();
			OAuthToken requestToken = new OAuthToken(oauth_token,oauth_verifier);
			AuthorizedRequestToken authorizedRequestToken = new AuthorizedRequestToken(requestToken, oauth_verifier);
			//コンフリクト
			if(StringUtils.isEmpty(token.getOauth_token())){
				OAuthToken accessToken= oauthOperations.exchangeForAccessToken(authorizedRequestToken, null);
				token.setOauth_token(accessToken.getValue());
				token.setOauth_verifier(accessToken.getSecret());
				System.out.println("Access Token:"+accessToken.getValue());
				System.out.println("Access Token Secret:"+accessToken.getSecret());
			}
		}
		if(!StringUtils.isEmpty(token.getOauth_token())){
			twitter = new TwitterTemplate("PgJdaamNXGzzKYWf5zEgdNmzN","tC7soU8JLmh72qpjLZJ2GbcpCC1Eek3lRp7mt3yRCBZyDAPSIL", token.getOauth_token(), token.getOauth_verifier());
			System.out.println(twitter.userOperations().getUserProfile().getName());
		}
		chain.doFilter(request, response);
		System.out.println("After!!");
	}

	private boolean isLoginCheckPath(String requestUri) {
		if(requestUri.contains("js/") || requestUri.contains("css/") || requestUri.contains("api/") || requestUri.contains("image/")){
			return true;
		}
		return false;
	}

	private boolean isLoginCheckPage(String nextUrl){
		if (!(isLoginCheckPath(nextUrl)) && connectionRepository.findPrimaryConnection(Twitter.class) == null
				&& !("/connect/twitter".equals(nextUrl))
				&& !("/".equals(nextUrl))
				&& StringUtils.isEmpty(token.getOauth_token())){
			return true;
		}
		return false;
	}

	@Override
	public void destroy() {
		System.out.println("destroy!!!");
	}
}
