package com.redhat.training.jb421;

import org.apache.camel.Exchange;
import org.apache.camel.jsonpath.JsonPath;

public class JSONPathCustomerBean {

	public static final String DEFAULT_PASSWORD = "changeme";
	public static final String ID_KEY = "id";

	/**
	 * Sample JSON Data: { "customer": { "id": "1", "firstName": "User",
	 * "lastName": "Name", "email": "user01@example.com", } }
	 */
	public void process(@JsonPath("$.customer.id") Integer id, @JsonPath("$.customer.firstName") String firstName,
			@JsonPath("$.customer.lastName") String lastName, @JsonPath("$.customer.email") String email,
			Exchange exchange) throws Exception {
		exchange.getIn().setHeader("id", id);
		exchange.getIn().setHeader("firstName", firstName);
		exchange.getIn().setHeader("lastName", lastName);
		exchange.getIn().setHeader("userName", firstName + "." + lastName);
		exchange.getIn().setHeader("password", DEFAULT_PASSWORD);
		exchange.getIn().setHeader("email", email);
		exchange.getIn().setHeader("admin", false);
	}

}
