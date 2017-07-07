package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformRouteBuilder extends RouteBuilder {
	
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void configure() throws Exception {
		from("direct:orderInput")
		//TODO add wiretap
		.wireTap("direct:jsonOrderLog")
		//TODO add filter
		.filter(new AdminOrderFilter())
		.to("mock:fulfillmentSystem");
		
		//TODO add second route
		from("direct:jsonOrderLog")
		.marshal().json(JsonLibrary.Jackson)
		.log("Order received: ${body}")
		.to("mock:orderLog");
	}
	
	

}
