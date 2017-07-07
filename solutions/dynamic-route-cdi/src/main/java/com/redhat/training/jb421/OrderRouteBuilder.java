package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

import com.redhat.training.jb421.model.Order;
public class OrderRouteBuilder extends RouteBuilder {

	
	@Override
	public void configure() throws Exception {
		
		JaxbDataFormat jaxbDataFormat = new JaxbDataFormat("com.redhat.training.jb421.model");
		from("jpa:com.redhat.training.jb421.model.Order?persistenceUnit=mysql"
				+ "&consumeDelete=false"
				+ "&consumer.namedQuery=getUndeliveredOrders"
				+ "&maximumResults=1"
				+ "&consumer.delay=30000"
				+ "&consumeLockEntity=false")
		.routeId("destination")
		.convertBodyTo(Order.class)
		.unmarshal(jaxbDataFormat)
		.log("${body}")
		.bean("destinationBean")
		.toD("${header.destination}");
//		.dynamicRouter(method(DestinationBean.class, "processDynamicRouterDestination"));
//		.routingSlip(header("destination"));
		
		//Show other dynamic routes as alternatives
	
	}

}
