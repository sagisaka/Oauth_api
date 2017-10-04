package com.spring.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.spring.app.model.Product;

public interface ProductsRepository
extends JpaRepository<Product, Integer> {
	List<Product> findByName(String find);
}
