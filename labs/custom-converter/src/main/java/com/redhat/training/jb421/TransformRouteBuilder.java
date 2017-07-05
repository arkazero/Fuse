package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;

import com.redhat.training.jb421.model.Order;

public class TransformRouteBuilder extends RouteBuilder {
	
	public static String SRC_FOLDER = "orders/incoming";

	//TODO add bindy data format
	BindyCsvDataFormat bindy = new BindyCsvDataFormat(Order.class); 
	
	@Override
	public void configure() throws Exception {
		from("file:"+SRC_FOLDER)
		//TODO add transform to update the CSV separator
		.transform(body().regexReplaceAll(",","`"))
		//TODO unmarshal with bindy
		.unmarshal(bindy)
		.to("mock:orderQueue","direct:orderLog");
		
		//TODO add second direct route
		
		from("direct:orderLog")
		.split(body())
		.log("${body}")
		.to("mock:orderLoggingSystem");
	}

}