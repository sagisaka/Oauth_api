package com.spring.app.service;


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
	private ProductsRepository productsRepository;
	@Autowired
	private LogProductsService logProductsServise;
	
	// 商品全件取得
	public List<Product> findAll() {
		return productsRepository.findAll();
	}

	// 商品一件取得
	public Product findOne(Integer id) {
		return productsRepository.findOne(id);
	}

	//商品の名前検索
	public List<Product> find(String name) {
		return productsRepository.findByName(name);
	}

	// 商品一件作成
	public Product create(Product product,String fileName,String author){
		product.setImageUrl(fileName);
		product.setAuthor(author);
		logProductsServise.createLogProduct(product,"create");
		return productsRepository.save(product);
	}

	// 商品一件更新
	public Product update(Product product, Product anotherProduct, String fileName,String author) {
		product.setName(anotherProduct.getName());
		product.setIntroduction(anotherProduct.getIntroduction());
		product.setPrice(anotherProduct.getPrice());
		product.setImageUrl(fileName);
		product.setAuthor(author);
		return productsRepository.save(product);
	}

	// 商品一件削除
	public void delete(Integer id) {
		logProductsServise.createLogProduct(this.findOne(id),"delete");
		productsRepository.delete(id);
	}
	
}
