package com.redhat.training.jb421;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.redhat.training.jb421.model.InventoryRequest;
import com.redhat.training.jb421.model.OrderItem;

public class InventoryRequestProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		OrderItem orderItem = exchange.getIn().getBody(OrderItem.class);
		InventoryRequest ir = new InventoryRequest();
		ir.setSku(orderItem.getSku());
		ir.setQuantity(orderItem.getQuantity());
		//Simulate processing time to enhance benefits of concurrency
		Thread.sleep(30);
		exchange.getIn().setBody(ir);
		exchange.getIn().setHeader("publisherId", orderItem.getCatalogItem().getPublisher().getId());
	}

}
