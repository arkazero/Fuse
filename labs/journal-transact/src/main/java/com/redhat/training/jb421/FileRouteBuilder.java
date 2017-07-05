package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

public class FileRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		//TODO add from() call to the route
		from("file:orders/incoming?include=order.*xml&delay=10000")
		//TODO add to() call to the route
			.to("file:orders?fileName=journal.txt&fileExist=Append");
		
	}

}
