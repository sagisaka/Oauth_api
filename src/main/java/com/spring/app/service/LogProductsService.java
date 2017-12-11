package com.spring.app.service;

import java.util.Calendar;
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
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		logProduct.setAuthor(product.getAuthor());
		logProduct.setLogTime(calendar);
		logProduct.setDay(calendar.get(Calendar.DATE));
		logProduct.setImageUrl(product.getImageUrl());
		logProduct.setIntroduction(product.getIntroduction());
		logProduct.setName(product.getName());
		logProduct.setPrice(product.getPrice());
		logProduct.setProductApi(api);
		logProductsRepository.save(logProduct);
	}

	public List<LogProduct> findByDay(Integer day) {
		return logProductsRepository.findByDay(day);
	}	
}
