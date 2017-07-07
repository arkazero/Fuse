package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

public class TransformRouteBuilder extends RouteBuilder {
	
	public static String OUTPUT_FOLDER = "/home/student/jb421/labs/transform-data/orders/outgoing";

	public static Long BATCH_TIMEOUT = 10000L;
	
	
	@Override
	public void configure() throws Exception {
		//TODO add jpa consumer
		
		//TODO add wire tap to second route
		
		//TODO marshal order to XML with JAXB
		
		//TODO split the order into individual order items
		
		//TODO aggregate the order items based on their catalog item id
		
		//TODO log the reservation XML to the console
		
		//TODO add file producer
		
		
		//TODO add second route to update order in the database
		
	}

}
