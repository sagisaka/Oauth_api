package com.spring.app.controller.rest;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cookie")
public class CookieRestController {

	@GetMapping(value="{delete}")
	public String CookieDelete(HttpServletRequest httpRequest,HttpServletResponse httpResponse) throws IOException{
		Cookie cookies[] = httpRequest.getCookies();
		for (Cookie cookie : cookies ) {
			if ("accessToken".equals(cookie.getName())) {
				cookie.setMaxAge(0);
				cookie.setPath("/");
				httpResponse.addCookie(cookie);
				return "OK";
			}
		}
		return "NO";
	}
}
