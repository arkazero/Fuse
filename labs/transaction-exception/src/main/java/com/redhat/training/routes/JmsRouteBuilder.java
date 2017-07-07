package com.redhat.training.routes;

import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.persistence.EntityManager;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jpa.JpaComponent;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.wildfly.extension.camel.CamelAware;

import com.redhat.training.model.Order;

import com.redhat.training.jms.transacted.JmsTransactionManager;

@Startup
@CamelAware
@ApplicationScoped
public class JmsRouteBuilder extends RouteBuilder {

	@Inject
	private EntityManager entityManager;

	@Inject
	private JmsTransactionManager transactionManager;

	@Resource(mappedName = "java:/JmsXA")
	private ConnectionFactory connectionFactory;

	@Override
	public void configure() throws Exception {

		JmsComponent jmsComponent = JmsComponent.jmsComponentTransacted(connectionFactory, transactionManager);
		getContext().addComponent("jms", jmsComponent);

		JpaComponent jpaComponent = new JpaComponent();
		jpaComponent.setEntityManagerFactory(entityManager.getEntityManagerFactory());
		jpaComponent.setTransactionManager(transactionManager);
		getContext().addComponent("jpa", jpaComponent);

		/**
		 * Configure JAXB so that it can discover model classes.
		 */
		JaxbDataFormat jaxbDataFormat = new JaxbDataFormat();
		jaxbDataFormat.setContextPath(Order.class.getPackage().getName());

		onException(SQLException.class)
		.maximumRedeliveries(1)
		.handled(true)
		.to("jms:queue:DeadLetter")
		.markRollbackOnly();

		from("file:/home/student/jb421/labs/transaction-exception/orders")
			.unmarshal(jaxbDataFormat)
				.to("jpa:com.redhat.training.model.Order");

		/**
		 *
		 * Whenever an order quantity greater than 10 is encountered, the route
		 * throws an IllegalStateException which forces the JMS / JPA
		 * transaction to be rolled back and the message to be delivered to the
		 * dead letter queue.
		 */
		from("jpa:com.redhat.training.model.Order")
//				 .transacted()
				// .transacted("PROPAGATION_MANDATORY")
				.to("jms:queue:OrdersQueue")
				.choice()
					.when(simple("${body.discount} > 100"))
						.log("Discount is greater than 100 - rolling back transaction!")
						.throwException(new SQLException())
					.otherwise()
						.log("Order processed successfully");

	}
}
