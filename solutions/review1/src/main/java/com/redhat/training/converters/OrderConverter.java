package com.redhat.training.converters;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;

import com.redhat.training.model.Order;
//TODO Annotate as a converter
@Converter
public class OrderConverter {
	@Converter
	public static String orderToString(Order order, Exchange exchange) {
		return order.toString();
	}
	
	@Converter
	public static InputStream orderToIStream(Order order, Exchange exchange) {
		ByteArrayInputStream bais = new ByteArrayInputStream(order.toString().getBytes());
		return bais;
	}
}
