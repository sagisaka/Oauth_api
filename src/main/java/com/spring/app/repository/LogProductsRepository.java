package com.spring.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.app.model.LogProduct;

public interface LogProductsRepository extends JpaRepository<LogProduct, Integer> {
	List<LogProduct> findByDay(Integer day);
}
