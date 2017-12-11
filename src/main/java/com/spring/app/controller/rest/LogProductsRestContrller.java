package com.spring.app.controller.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.app.model.LogProduct;
import com.spring.app.model.Product;
import com.spring.app.service.LogProductsService;

@RestController
@RequestMapping("log")
public class LogProductsRestContrller {
	
	@Autowired
	private LogProductsService logProductsServise;
	
	@PostMapping
	public List<LogProduct> getProduct(HttpServletResponse response,@RequestBody LogProduct logProduct) throws IOException {
		System.out.println(logProduct.getDay());
		List<LogProduct> products = logProductsServise.findByDay(logProduct.getDay());
		if(products.isEmpty()){
			response.sendError(HttpStatus.NOT_FOUND.value(),"データが見つかりませんでした");
		}
		System.out.println(products.get(0).getAuthor());

		return products;
	}
	
}
