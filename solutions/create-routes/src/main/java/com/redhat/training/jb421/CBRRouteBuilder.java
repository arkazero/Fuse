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
	        .process(new TestProcessor())
	        .filter(simple("${header.skipOrder} == null"))
	        //TODO use a processor to determine the vendor queue
	    	/* */
	    	.process(new DestinationProcessor())
	    	/* */
		    .choice()
		    	//TODO use CBR to route to the vendor queue
		        /*
		        .when(simple("${header.vendorName} == 'ABC Company'"))
		            .log("sending to queue abc")
		            .to(QUEUE_URI + "abc" + jmsOpts)
		        .when(simple("${header.vendorName} == 'ORly'"))
		            .log("sending to queue orly")
		            .to(QUEUE_URI + "orly" + jmsOpts)
		        .when(simple("${header.vendorName} == 'Namming'"))
		            .log("sending to queue namming")
		            .to(QUEUE_URI + "namming" + jmsOpts)
		        */
		    	/* */
		    	//TODO use toD to route to the vendor queue
		    	.when(simple("${header.queueName} != ''"))
		            .log("sending to queue: ${header.queueName}" )
		            .toD(QUEUE_URI + "${header.queueName}" + jmsOpts)
		        /* */
		        .otherwise()
		            .log("Cannot handle order: ${header.orderId}");
	}

}
