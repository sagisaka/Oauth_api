package com.spring.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="logProduct")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogProduct {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(nullable=false)
	@NotBlank(message = "文字を入力してください")
	@Size(max=100,message = "名前を100字以下にしてください")
	private String name;

	@Column(nullable=false)
	@NotBlank(message = "文字を入力してください")
	@Size(max=500,message = "説明を500字以下にしてください")
	private String introduction;

	@Column(nullable=false)
	@NotBlank(message = "文字を入力してください")
	@Pattern(regexp="[0-9]+$",message = "価格を数字にしてください")
	private String price;

	private String imageUrl;

	private String author;
	
	private String productApi;
	
	private String logDate;

}
