package com.redhat.training.jb421;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimerBean {

	private static final int LOG_EVERY = 100;

	private static Logger log = LoggerFactory.getLogger(TimerBean.class);

	private AtomicLong startTime = new AtomicLong();
	private AtomicInteger itemNum = new AtomicInteger();

	public void start(){
		if(startTime.get() == 0){
			this.startTime.set(new Date().getTime());
		}
	}

	public void stop(){
		itemNum.incrementAndGet();
		if(itemNum.get() % LOG_EVERY == 0 && itemNum.get() <= 1500){
			Date stopTime = new Date();
			Long timeElapsedInSec = ((stopTime.getTime() - startTime.get())/1000 );
			log.info("Item Number " + itemNum +" Processing complete! Time elapsed: "+ timeElapsedInSec  + " seconds");
		}
	}

}
