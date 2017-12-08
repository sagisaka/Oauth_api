package com.spring.app.controller.periodic;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spring.app.LoggingFilter;
import com.spring.app.service.ProductsService;

public class JobPeriodical {

	private Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

	
	public void oneDayJob(ProductsService productService){
		TimerTask task = new TimerTask() {
			public void run() {
				if(!productService.findAll().isEmpty()){
					logger.info(("Time" + productService.findAll().get(0).getCreateTime()));
				}else{
					logger.info("no");
				}
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 0, 5000L);
	}

}
