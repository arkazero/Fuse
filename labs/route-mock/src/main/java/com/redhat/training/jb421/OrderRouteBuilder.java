package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

import com.redhat.training.jb421.predicates.Predicates;

public class OrderRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("direct:orderHeader")
		.routeId("orderHeader")
		.filter(Predicates.bodyAndHeaderNotNull())
			.to("mock:secondRoute");
	}

}
