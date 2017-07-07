package com.redhat.training.jb421;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import com.redhat.training.jb421.model.OrderItem;
import com.redhat.training.jb421.model.Reservation;

public class ReservationAggregationStrategy implements AggregationStrategy {
	
	
	 public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
	        OrderItem newBody = newExchange.getIn().getBody(OrderItem.class);
	        Reservation reservation = null;
	        if (oldExchange == null) {
	            reservation = new Reservation();
	            reservation.setCatalogItemId(newBody.getCatalogItem().getId());
	            reservation.setReservationDate(new Date());
	            reservation.setQuantity(newBody.getQuantity());
	            newExchange.getIn().setBody(reservation);
	            return newExchange;
	        } else {
	            reservation = oldExchange.getIn().getBody(Reservation.class);
	            reservation.setQuantity(reservation.getQuantity() + newBody.getQuantity());
	            return oldExchange;
	        }
	    }
	
	

}
