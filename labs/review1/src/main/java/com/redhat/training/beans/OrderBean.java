
package com.redhat.training.beans;

import java.util.UUID;

import org.apache.camel.Exchange;

import com.redhat.training.model.Order;

public class OrderBean {

    public Order processOrder(Exchange exchange, Order order) {
    	//TODO Set filename associated with this order.
    	//TODO Set order state to **
        return order;
    }
    
	public String generateRandomFileName() {
		return UUID.randomUUID().toString();
	}
}