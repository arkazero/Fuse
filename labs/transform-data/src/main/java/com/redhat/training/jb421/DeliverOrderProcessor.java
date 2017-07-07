package com.redhat.training.jb421;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.redhat.training.jb421.model.Order;

public class DeliverOrderProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Order order = exchange.getIn().getBody(Order.class);
		order.setDelivered(true);
	}

}
