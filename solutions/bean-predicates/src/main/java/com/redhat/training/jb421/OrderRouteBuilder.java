package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

import com.redhat.training.jb421.processor.InvalidValueExceptionProcessor;

public class OrderRouteBuilder extends RouteBuilder {
	
	
	@Override
	public void configure() throws Exception {
		
		errorHandler(deadLetterChannel("mock:invalidValueOrders").disableRedelivery());
		
		from("file:orders")
				.choice()
					.when(method("negativeBean","isNegative"))
						.to("mock:invalidValueOrders")
				//TODO Use predicate silverBean
					.when(method("silverBean","isSilver"))
						.to("mock:silverEndpoint")
				//TODO Use predicate GoldBean
					.when(method("goldBean","isGold"))
						.to("mock:goldEndpoint")
				//TODO Use predicate eliteBean
					.when(method("eliteBean","isElite"))
						.to("mock:eliteEndpoint")
					.otherwise()
				//TODO Call Processor
						.process(new InvalidValueExceptionProcessor())
				.endChoice()
			;
			
			
			
	}

}
