package com.redhat.training.jb421.beans;

import java.math.BigDecimal;
import java.util.List;

import com.redhat.training.jb421.model.OrderItem;

/**
 * Utility bean to calculate the total cost of an order given a list of order items
 *
 */
public class OrderTotalBean {
	
	public BigDecimal getOrderTotal(List<OrderItem> items){
		
		BigDecimal total = new BigDecimal(0.0);
		for(OrderItem item: items){
			total = total.add((item.getExtPrice().multiply(new BigDecimal(item.getQuantity()))));
		}
		return total;
		
	}
	
}
