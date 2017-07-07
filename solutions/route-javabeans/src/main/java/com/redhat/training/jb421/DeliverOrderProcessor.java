package com.redhat.training.jb421;

import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.redhat.training.jb421.model.Order;

//TODO Annotate with CDI
@Named("deliverOrder")
public class DeliverOrderProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Order order = exchange.getIn().getBody(Order.class);
		order.deliver();
	}

}
