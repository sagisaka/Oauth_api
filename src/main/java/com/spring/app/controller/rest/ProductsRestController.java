package com.spring.app.controller.rest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.app.model.OauthToken;
import com.spring.app.model.Product;
import com.spring.app.service.OauthTokenService;
import com.spring.app.service.ProductsService;

@RestController
@RequestMapping("api/product")
public class ProductsRestController {
	@Autowired
	private ProductsService productService;
	@Autowired
	private OauthTokenService oauthTokenService;

	// 商品全件取得
	@GetMapping
	public List<Product> getProduct(HttpServletResponse response) throws IOException {
		List<Product> products = productService.findAll();
		if(products.isEmpty()){
			response.sendError(HttpStatus.NOT_FOUND.value(),"データが見つかりませんでした");
		}
		return products;
	}

	// 商品一件取得
	@GetMapping(value="{id:[0-9]+$}")
	public Product getProduct(@PathVariable Integer id,HttpServletResponse response) throws IOException {
		Product product = productService.findOne(id);
		if(product == null){
			response.sendError(HttpStatus.NOT_FOUND.value(),"データが見つかりませんでした");
		}
		return product;
	} 	

	//　商品取得
	@PostMapping(value="/sam")
	public List<Product> getValueproduct(HttpServletResponse response, @RequestBody Product product) throws IOException {
		List<Product> products = productService.find(product.getName());
		if(products.isEmpty()){
			response.sendError(HttpStatus.NOT_FOUND.value(),"データが見つかりませんでした");
		}
		return products;
	}

	// 商品一件更新
	@PostMapping(value="{id:[0-9]+$}")
	public Product putProduct(HttpServletRequest httpRequest,@PathVariable Integer id, HttpServletResponse response, @Valid Product anotherProduct, BindingResult result, @RequestParam MultipartFile file) throws IOException {
		if (result.hasErrors()) response.sendError(HttpStatus.BAD_REQUEST.value(),"空文字か値段が文字列です");
		Product product = productService.findOne(id);
		if(product == null){
			response.sendError(HttpStatus.NOT_FOUND.value(),"データが見つかりませんでした");
		}
		try(BufferedInputStream in = new BufferedInputStream(file.getInputStream());
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("src/main/resources/static/image/" + file.getOriginalFilename()))) {
			FileCopyUtils.copy(in, out);
		} catch (IOException e) {
			response.sendError(HttpStatus.BAD_REQUEST.value(),e.getMessage());
		}
		List<OauthToken> oauthToken = oauthTokenService.findByApiAccessToken(httpRequest.getHeader("Authorization"));
		if(!oauthToken.isEmpty()){
			return productService.update(product,anotherProduct,file.getOriginalFilename(),oauthToken.get(0).getAuthor());
		}
		return null;
	}


	// 商品一件削除
	@DeleteMapping(value="{id:[0-9]+$}")
	public void deleteProduct(HttpServletResponse response,@PathVariable Integer id) throws IOException {
		Product product = productService.findOne(id);
		if(product == null){
			response.sendError(HttpStatus.NOT_FOUND.value(),"データが見つかりませんでした");
		}
		productService.delete(id);
	}

	// 商品一件登録
	@PostMapping
	public Product handle(HttpServletRequest httpRequest,HttpServletResponse response, @Valid Product product, BindingResult result, @RequestParam MultipartFile file) throws IOException{
		if (result.hasErrors()) response.sendError(HttpStatus.BAD_REQUEST.value(),"空文字か値段が文字列です");
		try(BufferedInputStream in = new BufferedInputStream(file.getInputStream());
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("src/main/resources/static/image/" + file.getOriginalFilename()))) {
			FileCopyUtils.copy(in, out);
		} catch (IOException e) {
			response.sendError(HttpStatus.NOT_FOUND.value(),e.getMessage());
		}
		List<OauthToken> oauthToken = oauthTokenService.findByApiAccessToken(httpRequest.getHeader("Authorization"));
		if(!oauthToken.isEmpty()){
			return productService.create(product,file.getOriginalFilename(),oauthToken.get(0).getAuthor());
		}
		return null;
	}

}