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
import com.spring.app.service.LogProductsService;

@RestController
@RequestMapping("api/log")
public class LogProductsRestContrller {
	
	@Autowired
	private LogProductsService logProductsServise;
	
	//日付とアウトプット可能データの検索
	@PostMapping
	public List<LogProduct> getProduct(HttpServletResponse response,@RequestBody LogProduct logProduct) throws IOException {
		List<LogProduct> products = logProductsServise.findByLogDate(logProduct.getLogDate());
		List<LogProduct> outputProducts = logProductsServise.findBycheckOutput(products);
		if(outputProducts.isEmpty()){
			response.sendError(HttpStatus.NOT_FOUND.value(),"データが見つかりませんでした");
		}
		return outputProducts;
	}
}
