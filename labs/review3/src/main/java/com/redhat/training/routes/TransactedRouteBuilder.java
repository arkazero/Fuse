package com.redhat.training.routes;

import org.apache.camel.builder.RouteBuilder;

public class TransactedRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		//TODO Instantiate the JaxbDataFormat
		//TODO Manage the java.net.ConnectException to be handled and rollback the transaction
	

		//route from the order
		from("")
			.routeId("MessageToDB")
			//TODO define the route as transacted
			.log("Message: ${body}")
			//TODO convert the received object (an OrderReceived object to an Order object)
			//TODO store at the db using JPA
			.to("jpa:com.redhat.training.model.Order")
		;
		//route to get all the orders.
		from("jpa:com.redhat.training.model.Order?consumer.namedQuery=allOrders")
			.routeId("FromDB")
			.log("Order from DB: ${body}")
			//TODO define the destinations to activemq queues.
			//TODO convert to XML
			//TODO send them to destinations using the Recipient list EIP
		;
		
	}

}

