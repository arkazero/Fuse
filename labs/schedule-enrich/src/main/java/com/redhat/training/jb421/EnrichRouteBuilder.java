package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

public class EnrichRouteBuilder extends RouteBuilder {


	@Override
	public void configure() throws Exception {
		
		//TODO use jpa consumer
		from("mock:start")
		//TODO add wiretap
		
		//TODO add enrich call
		
		.log("Order sent to fulfillemnt: ${body}")
		.to("mock:fulfillmentSystem");
		
		//TODO add direct:enrichOrder route
		
		//TODO add direct:updateOrder route
		
	}

}
