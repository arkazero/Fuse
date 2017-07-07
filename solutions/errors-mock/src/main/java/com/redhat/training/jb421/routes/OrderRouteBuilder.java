package com.redhat.training.jb421.routes;

import org.apache.camel.builder.RouteBuilder;

import com.redhat.training.jb421.processor.InvalidStateProcessor;
import com.redhat.training.jb421.processor.NullStateProcessor;

public class OrderRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		//TODO Manage error
		errorHandler(deadLetterChannel("mock:invalidStateEndpoint").disableRedelivery());
		from("file:orders")
			.choice()
				//TODO Check if the state attribute is empty
				.when(method("emptyState","emptyState"))
					.process(new NullStateProcessor())
				//TODO Check if the state attribute is valid
				.when(method("invalidState","invalidState"))
					.process(new InvalidStateProcessor())
				.otherwise()
					.setHeader("state",xpath("/order/customer/shippingAddress/state/text()"))
					.toD("mock:${header.state}")
				.endChoice()
				;
	}

}

