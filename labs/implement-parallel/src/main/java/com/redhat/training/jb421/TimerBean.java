package com.redhat.training.jb421;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimerBean {
	
	private static final int ITEMS_PER_ORDER = 300;
	
	private static Logger log = LoggerFactory.getLogger(TimerBean.class);
	
	private AtomicLong startTime = new AtomicLong();
	private AtomicInteger orderItemNum = new AtomicInteger();
	
	public void start(){
		if(startTime.get() == 0){
			this.startTime.set(new Date().getTime());
		}
	}
	
	public void stop(){
		orderItemNum.incrementAndGet();
		if(orderItemNum.get() % ITEMS_PER_ORDER == 0){
			Date stopTime = new Date();
			Long timeElapsedInSec = ((stopTime.getTime() - startTime.get())/1000 );
			int orderNum = (orderItemNum.get() / ITEMS_PER_ORDER);
			log.info("Order " + orderNum +" Processing complete! Time elapsed: "+ timeElapsedInSec  + " seconds");
		}
	}

}
