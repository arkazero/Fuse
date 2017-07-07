package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;

//@ContextName("logistics-context")
public class OrderRouteBuilder extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
		from("file:orders")
		.routeId("destination")
		.split().tokenize("\n")
		.bean("destinationBean")
		.to("file:destination");
//		.toD("${header.destination}");
//		.dynamicRouter(method(DestinationBean.class, "processDynamicRouterDestination"));
//		.routingSlip(header("destination"));
		
	}

}
