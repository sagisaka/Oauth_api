package com.spring.app.controller.periodic;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spring.app.LoggingFilter;
import com.spring.app.model.LogProduct;
import com.spring.app.service.LogProductsService;

public class JobPeriodical {

	private Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
	
	//1日一回実行
	public void oneDayJob(LogProductsService logProductService){
		TimerTask task = new TimerTask() {
			public void run() {
				//登録したログデータからアウトプットする
				List<LogProduct> logProducts = logProductService.findByCheckOutput(false);
				if(logProducts.isEmpty()){
					logger.info("Nothing");
				}else{
					logProductService.updateCheckOutput(logProducts);
					logger.info("Output Data");
				}
			}
		};
		Timer timer = new Timer();
		//1日
		timer.schedule(task, 0, 60000*60*24);
	}

}
