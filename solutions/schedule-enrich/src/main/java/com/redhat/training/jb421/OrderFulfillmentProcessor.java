package com.redhat.training.jb421;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.redhat.training.jb421.model.Order;

public class OrderFulfillmentProcessor implements Processor{
	
	@Override
	public void process(Exchange exchange) throws Exception {
		Order incoming = exchange.getIn().getBody(Order.class);
		incoming.setDateFulfilled(new Date());
		incoming.setFulfilledBy("admin");
	}

}
