package com.redhat.training.jb421;

import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class CBRRouteBuilder extends RouteBuilder {

	final private static Logger log = LoggerFactory.getLogger(CBRRouteBuilder.class);

	final private static String SRC_URI = "file:/tmp/orders/incoming";
	final private static String QUEUE_URI = "jms:queue:";
	final private static String QUEUE_OPTS= "?username={{jms.username}}&password={{jms.password}}";

	final private static String XPATH_VENDOR_NAME = "/order/orderItems/orderItem/orderItemPublisherName/text()";
	final private static String XPATH_ORDERID = "/order/orderId/text()";

	@PropertyInject("jms.auth")
    private String jmsAuth;
	
	@Override
	public void configure() throws Exception {
		
		String jmsOpts = "true".equals(jmsAuth) ? QUEUE_OPTS : "";
		
		from(SRC_URI)
			.convertBodyTo(Document.class)
			.setHeader("orderId", xpath(XPATH_ORDERID))
	        .log("processing order: ${header.orderId}")
	        .setHeader("vendorName", xpath(XPATH_VENDOR_NAME))
	        .log("vendor: ${header.vendorName}")
	        //TODO use a processor and a filter to skip test orders
	        //TODO use a processor to determine the vendor queue
		    .choice()
		        //TODO use CBR to route to the vendor queue
		        //TODO use toD and a processor to route to the vendor queue
		        .otherwise()
		            .log("Cannot handle order: ${header.orderId}");
	}

}
