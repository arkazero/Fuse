
package com.redhat.training.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

public class JmsRouteBuilder extends RouteBuilder {

	public static final String DIRECTORY = "/home/student/jb421/solutions/explore-transaction/orders";

	@Override
	public void configure() throws Exception {


		// TODO Instantiate the JaxbDataFormat
		JaxbDataFormat jaxb = new JaxbDataFormat("com.redhat.training.model");
		onException(IllegalStateException.class).handled(true).log("Connection to ActiveMQ failed. Will re-attempt.")
				.markRollbackOnly();

		from("file:"+DIRECTORY)
		.unmarshal(jaxb)
			.to("jpa:com.redhat.training.model.Order");
		
		// route from the order
		from("jpa:com.redhat.training.model.Order").routeId("MessageToDB")
				// TODO define the route as transacted
				.transacted()
				// TODO store in a queue on ActiveMQ
				.to("jms:queue:ordersProcessed")
				.choice()
					.when(simple("${body.discount} > 100"))
						.log("Order discount is greater than 100 - rolling back transaction!")
						.throwException(new IllegalStateException())
					.otherwise()
						.log("Order processed successfully");
		
	}

}