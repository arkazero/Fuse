package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

public class CustomerRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		//Create Customer
		from("restlet:http://0.0.0.0:8080/customers?restletMethod=POST")
			.convertBodyTo(String.class)
			.log("Creating new customer ${body}")
			.bean(new JSONPathCustomerBean())
			.to("sql:insert into bookstore.Customer (id, firstName, lastName, userName, "
					+ "password, email, admin) values (:#id, :#firstName, :#lastName, "
					+ ":#userName, :#password, :#email, :#admin)"
					+ "?dataSource=mysqlDataSource");

		//Update Customer to add Address
		from("restlet:http://0.0.0.0:8080/customers?restletMethod=PUT")
			.convertBodyTo(String.class)
			.bean(new JSONPathAddressBean())
			.log("Updating customer with id ${header.customerId} to add address ${body}")
			.to("sql:insert into bookstore.Address (id,streetAddress1,streetAddress2,"
					+ "streetAddress3,city,state,postalCode,country) values "
					+ "(:#id, :#streetAddress1, :#streetAddress2, :#streetAddress3, "
					+ ":#city, :#state, :#postalCode, :#country)"
					+ "?dataSource=mysqlDataSource")
			.choice()
				.when(body().contains("billing"))
					.to("sql:update bookstore.Customer set bill_addr_id=:#id where id = (:#customerId)"
							+ "?dataSource=mysqlDataSource")
				.otherwise()
					.to("sql:update bookstore.Customer set ship_addr_id=:#id where id = (:#customerId)"
							+ "?dataSource=mysqlDataSource")
			.end();

		//Delete Customer and Addresses
		from("restlet:http://0.0.0.0:8080/customers?restletMethod=DELETE")
			.convertBodyTo(String.class)
			.log("Deleting customer ${body}")
			.bean(new JSONPathCustomerBean())
			.to("sql:delete from bookstore.Address where id in"
					+ " (select bill_addr_id from bookstore.Customer"
					+ " where id = (:#id))"
					+ " OR id in"
					+ " (select ship_addr_id  from bookstore.Customer"
					+ " where id = (:#id))"
					+ "?dataSource=mysqlDataSource")
			.to("sql:delete from bookstore.Customer where id = (:#id)?dataSource=mysqlDataSource");
	}
}
