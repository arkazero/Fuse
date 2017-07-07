
package com.redhat.training.routes;

import java.net.ConnectException;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JaxbDataFormat;

import com.redhat.training.beans.DestinationBean;
import com.redhat.training.model.Order;

public class TransactedRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// TODO Instantiate the JaxbDataFormat
		JaxbDataFormat jaxb = new JaxbDataFormat(true);
		// TODO Manage the java.net.ConnectException to be handled and rollback the
		// transaction
		onException(ConnectException.class).handled(true).log("Connection to database failed. Will re-attempt.")
				.markRollbackOnly();

		// route from the order
		from("activemq:queue:orders?transacted=true").routeId("MessageToDB")
				// TODO define the route as transacted
				.transacted().log("Message: ${body}")
				// TODO convert the received object (an OrderReceived object to
				// an Order object)
				.convertBodyTo(Order.class)
				// TODO store at the db using JPA
				.to("jpa:com.redhat.training.model.Order");
		// route to get all the orders.
		from("jpa:com.redhat.training.model.Order?consumer.namedQuery=allOrders").routeId("FromDB")
				.log("Order from DB: ${body}")
				// TODO define the destinations to activemq queues.
				.setHeader("recipients", method(DestinationBean.class))
				// TODO convert to XML
				.marshal(jaxb)
				// TODO send them to destinations using the Recipient list EIP
				.recipientList(header("recipients"));

	}

}
