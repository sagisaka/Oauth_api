package com.spring.app.service;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.app.model.Product;
import com.spring.app.repository.ProductsRepository;

@Service
@Transactional
public class ProductsService {
	@Autowired
	private ProductsRepository repository;
	
	// 商品全件取得
	public List<Product> findAll() {
		return repository.findAll();
	}

	// 商品一件取得
	public Product findOne(Integer id) {
		return repository.findOne(id);
	}

	//商品の名前検索
	public List<Product> find(String name) {
		return repository.findByName(name);
	}

	// 商品一件作成
	public Product create(Product product,String fileName,String author){
		product.setImageUrl(fileName);
		product.setAuthor(author);
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		product.setCreateTime(calendar);
		return repository.save(product);
	}

	// 商品一件更新
	public Product update(Product product, Product anotherProduct, String fileName,String author) {
		product.setName(anotherProduct.getName());
		product.setIntroduction(anotherProduct.getIntroduction());
		product.setPrice(anotherProduct.getPrice());
		product.setImageUrl(fileName);
		product.setAuthor(author);
		return repository.save(product);
	}

	// 商品一件削除
	public void delete(Integer id) {
		repository.delete(id);
	}
	public Integer findAllSize() {
		return repository.findAll().size();
	}
	
}
