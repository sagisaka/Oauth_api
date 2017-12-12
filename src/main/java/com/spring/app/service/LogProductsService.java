package com.spring.app.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
		logProduct.setCheckOutput(false);
		logProductsRepository.save(logProduct);
	}
	
	//日付データの同値判別
	public List<LogProduct> findByLogDate(String date) {
		return logProductsRepository.findByLogDate(date);
	}
	
	//アウトプットデータ判別
	public List<LogProduct> findByCheckOutput(Boolean check) {
		return logProductsRepository.findByCheckOutput(check);
	}
	
	//アウトプットデータに変更
	public void updateCheckOutput(List<LogProduct> logProducts) {
		logProducts.forEach(logProduct->{
			logProduct.setCheckOutput(true);
			logProductsRepository.save(logProduct);
		});
	} 
	
	//アウトプットデータを追加
	public List<LogProduct> findBycheckOutput(List<LogProduct> products) {
		List<LogProduct> outputProducts = new ArrayList<LogProduct>();
		products.forEach(product->{
			if(product.getCheckOutput()) outputProducts.add(product);
		});
		return outputProducts;
	}	
}
