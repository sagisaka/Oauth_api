package com.spring.app.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@ControllerAdvice
@RequestMapping("/error")
public class AppErrorController implements ErrorController {

	private final ErrorAttributes errorAttributes;

	@Autowired
	public AppErrorController(ErrorAttributes errorAttributes) {
		Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
		this.errorAttributes = errorAttributes;
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

	private boolean getTraceParameter(HttpServletRequest request) {
		String parameter = request.getParameter("trace");
		if (parameter == null) {
			return false;
		}
		return !"false".equals(parameter.toLowerCase());
	}

	private Map<String, Object> getErrorAttributes(HttpServletRequest aRequest, boolean includeStackTrace) {
		RequestAttributes requestAttributes = new ServletRequestAttributes(aRequest);
		return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
	}

	@RequestMapping
	public Map<String, Object> getErrorBasic(HttpServletRequest aRequest){
		Map<String, Object> body = getErrorAttributes(aRequest,getTraceParameter(aRequest));
		body.remove("path");
		if(body.get("status").equals(HttpStatus.NOT_FOUND.value())){
			body.put("status",HttpStatus.NOT_FOUND.value());
			if(body.get("message").equals("No message available")){ 
				body.put("message", "ページが見つかりませんでした");
			}
			body.put("error", "Not Found");		
		}
		return body;
	}

	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Map<String, Object> getErrorRequest(HttpServletRequest aRequest){
		Map<String, Object> body = getErrorAttributes(aRequest,getTraceParameter(aRequest));
		body.remove("exception");
		body.put("status", HttpStatus.METHOD_NOT_ALLOWED.value());
		body.put("message", "許可されていないメソッドです");
		body.put("error", "Method Not Allowed");
		return body;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public Map<String, Object> getAnotherErrorRequest(HttpServletRequest aRequest){
		Map<String, Object> body = getErrorAttributes(aRequest,getTraceParameter(aRequest));
		body.remove("exception");
		body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		body.put("error", "Internal Server Error");
		return body;
	}
}