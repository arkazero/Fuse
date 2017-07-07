package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;

import com.redhat.training.jb421.model.Order;

public class TransformRouteBuilder extends RouteBuilder {
	
	public static String SRC_FOLDER = "orders/incoming";

	BindyCsvDataFormat bindy = new BindyCsvDataFormat(Order.class);
	
	@Override
	public void configure() throws Exception {
		from("file:"+SRC_FOLDER)
		.transform(body().regexReplaceAll(",", "`"))
		.unmarshal(bindy)
		.to("mock:orderQueue","direct:orderLog");
		
		from("direct:orderLog")
		.split(body())
		.log("${body}")
		.to("mock:orderLoggingSystem");
		
	}

}