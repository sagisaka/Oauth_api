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
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;

@Component
public class LoggingFilter implements Filter {

	private ConnectionRepository connectionRepository;


	@Inject
	public LoggingFilter(ConnectionRepository connectionRepository) {
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
				&& !("/".equals(nextUrl))){
			return true;
		}
		return false;
	}

	@Override
	public void destroy() {
		System.out.println("destroy!!");
	}
}
