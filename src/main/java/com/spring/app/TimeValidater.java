package com.spring.app;

import java.util.Calendar;

public class TimeValidater {
	//有効期限チェック
	public boolean isPeriodValidation(Calendar validationTime) {
		Calendar culendar = Calendar.getInstance();
		return culendar.before(validationTime);
	}
}
