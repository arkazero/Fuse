package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;

import com.redhat.training.jb421.model.Order;

public class TransformRouteBuilder extends RouteBuilder {
	
	public static String SRC_FOLDER = "orders/incoming";

	//TODO add bindy data format
	
	@Override
	public void configure() throws Exception {
		from("file:"+SRC_FOLDER)
		//TODO add transform to update the CSV separator
		
		//TODO unmarshal with bindy
		
		.to("mock:orderQueue");
		
		//TODO add second direct route
	}

}