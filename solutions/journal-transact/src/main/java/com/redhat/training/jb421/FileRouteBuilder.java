package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

public class FileRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		//DONE add from() call to the route
		from("file:orders/incoming?include=order.*xml")
		//DONE add to() call to the route
		.to("file:orders?fileName=journal.txt&fileExist=Append");
	}

}
