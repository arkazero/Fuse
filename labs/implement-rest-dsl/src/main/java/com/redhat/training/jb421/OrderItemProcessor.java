package com.redhat.training.jb421;

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.redhat.training.jb421.model.Order;
import com.redhat.training.jb421.model.OrderItem;

public class OrderItemProcessor implements Processor {

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		Order o = exchange.getIn().getHeader("order", Order.class);
		List<OrderItem> items = (List<OrderItem>) exchange.getIn().getBody();
		
		if(o != null){
			o.getOrderItems().addAll(items);
		}else{
			throw new NoResultException("No order was found for the given ID");
		}
		exchange.getIn().setBody(o);
	}

}
