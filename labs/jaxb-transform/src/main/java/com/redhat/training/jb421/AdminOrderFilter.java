package com.redhat.training.jb421;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.training.jb421.model.Order;

public class AdminOrderFilter implements Predicate {

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean matches(Exchange exchange) {
		Order order = exchange.getIn().getBody(Order.class);
		//filter out any orders where the customer is an admin
		if(order != null && order.getCustomer() != null && order.getCustomer().isAdmin()){
			log.info("Filtering out admin order!");
			return false;
		}
		return true;
	}

}
