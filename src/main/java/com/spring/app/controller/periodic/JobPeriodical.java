package com.spring.app.controller.periodic;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.spring.app.LoggingFilter;
import com.spring.app.model.LogProduct;
import com.spring.app.service.LogProductsService;

@Component
public class JobPeriodical {

	private Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

	@Autowired
	private LogProductsService logProductService;

	//0時に一回実行
	@Scheduled(cron = "0 * * * * *", zone = "Asia/Tokyo")
	public void oneDayJob(){
		//登録したログデータからアウトプットする
		List<LogProduct> logProducts = logProductService.findByCheckOutput(false);
		if(logProducts.isEmpty()){
			logger.info("Nothing");
		}else{
			logProductService.updateCheckOutput(logProducts);
			logger.info("Output Data");
		}
	}
}
