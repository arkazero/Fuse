package com.redhat.training.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JaxbDataFormat;

import com.redhat.training.model.Contact;


public class JmsRouteBuilder extends RouteBuilder {

	public static final String DIRECTORY = "/home/student/jb421/solutions/test-transaction/contact";

	@Override
	public void configure() throws Exception {


		/**
		 * Configure JAXB so that it can discover model classes.
		 */
		JaxbDataFormat jaxbDataFormat = new JaxbDataFormat();
		jaxbDataFormat.setContextPath(Contact.class.getPackage().getName());

		onException(IllegalStateException.class)
		.maximumRedeliveries(1)
		.handled(true)
		.to("jms:queue:DeadLetter")
		.markRollbackOnly();

		// TODO Reading files that will be processed
		from("file:"+JmsRouteBuilder.DIRECTORY)
			.unmarshal(jaxbDataFormat)
				.to("jpa:com.redhat.training.model.Contact");

		/**
		 *
		 * Whenever an email is empty, the route
		 * throws an IllegalStateException which forces the JMS / JPA
		 * transaction to be rolled back and the message to be delivered to the
		 * dead letter queue.
		 */
		from("jpa:com.redhat.training.model.Contact")
				.routeId("MessageToDB")
				//TODO Configure as transacted
				.transacted()
				.to("jms:queue:ContactsQueue")
				.choice()
					.when(simple("${body.email} == null"))
						.log("Invalid email - rolling back transaction!")
						.throwException(new IllegalStateException())
					.otherwise()
						.log("Contact processed successfully");

	}
}
