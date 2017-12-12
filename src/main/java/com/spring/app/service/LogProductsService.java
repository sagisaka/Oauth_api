package com.spring.app.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.app.model.LogProduct;
import com.spring.app.model.Product;
import com.spring.app.repository.LogProductsRepository;

@Service
@Transactional
public class LogProductsService {

	@Autowired
	private LogProductsRepository logProductsRepository;

	public void createLogProduct(Product product,String api){
		LogProduct logProduct = new LogProduct();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate = new Date();
		String logDate = df.format(nowDate);
		logProduct.setAuthor(product.getAuthor());
		logProduct.setLogDate(logDate);
		logProduct.setImageUrl(product.getImageUrl());
		logProduct.setIntroduction(product.getIntroduction());
		logProduct.setName(product.getName());
		logProduct.setPrice(product.getPrice());
		logProduct.setProductApi(api);
		logProductsRepository.save(logProduct);
	}

	public List<LogProduct> checkByDate(String date) {
		return logProductsRepository.findByLogDate(date);
	}	
}
