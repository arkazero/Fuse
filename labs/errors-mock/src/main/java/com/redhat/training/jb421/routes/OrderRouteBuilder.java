package com.redhat.training.jb421.routes;

import org.apache.camel.builder.RouteBuilder;

import com.redhat.training.jb421.processor.InvalidStateProcessor;
import com.redhat.training.jb421.processor.NullStateProcessor;

public class OrderRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		//TODO Manage error
		from("file:orders")
			.choice()
				//TODO Check if the state attribute is empty
				//TODO Check if the state attribute is valid
				.otherwise()
					.toD("mock:${header.state}")
				.endChoice()
				;
	}

}

