package com.spring.app.controller.web;

import java.util.List;

import javax.inject.Inject;
<<<<<<< HEAD
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
=======
>>>>>>> origin/master

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
<<<<<<< HEAD
import org.springframework.social.twitter.api.impl.TwitterTemplate;
=======
>>>>>>> origin/master
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

=======
>>>>>>> origin/master
import com.spring.app.model.Product;
import com.spring.app.service.ProductsService;

@Controller
public class WebController {
	@Autowired
	ProductsService service;

	private Twitter twitter;

	private ConnectionRepository connectionRepository;

	@Inject
	public WebController(Twitter twitter, ConnectionRepository connectionRepository) {
		this.twitter = twitter;
		this.connectionRepository = connectionRepository;
	}
<<<<<<< HEAD
	
	@GetMapping("/login")
	public String login() {
=======

	@GetMapping("/login")
	public String login(Model model) {
>>>>>>> origin/master
		return "redirect:/connect/twitter";
	}

	@GetMapping("/twitter")
	public String hello(Model model) {
<<<<<<< HEAD
=======
		//このページをログイン後にするための設定をしたい
>>>>>>> origin/master
		if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
			return "redirect:/login";
		}
		CursoredList<TwitterProfile> friends = twitter.friendOperations().getFriends();
		List<Tweet> tweets = twitter.timelineOperations().getHomeTimeline();
		model.addAttribute(twitter.userOperations().getUserProfile());
		model.addAttribute("friends", friends);
		model.addAttribute("tweets",tweets);
<<<<<<< HEAD
		return "twitters";
	}
	
=======
		return "main";
	}
>>>>>>> origin/master
	@GetMapping("/logout")
	public String logout() {
		connectionRepository.removeConnections("twitter");
		return "logout";
	}

	@GetMapping(value="/")
	public String index() {
		return "index";
	}

	@GetMapping(value="/{id}")
	public String detail(@PathVariable("id") String id, Model model) {
		try {
			Product product = service.findOne(Integer.parseInt(id));
			if(product == null){
				return "nullDetail";
			}
			model.addAttribute("image","/image/"+product.getImageUrl());
			model.addAttribute("introduction",product.getIntroduction());
			model.addAttribute("price",product.getPrice()+"円");
			model.addAttribute("data",service.findOne(Integer.parseInt(id)));
			return "detail";
		} catch (NumberFormatException e) {
			return "nullDetail";
		}				
	}

	@GetMapping(value="/create")
	public String create() {
		return "create";
	}
}
