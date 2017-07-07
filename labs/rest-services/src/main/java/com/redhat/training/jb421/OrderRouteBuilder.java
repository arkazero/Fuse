package com.redhat.training.jb421;

import java.util.List;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.dataformat.JsonLibrary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.redhat.training.jb421.model.Address;

public class OrderRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		//TODO create two REST services using Restlet, CXFRS or REST DSL

		// routes that implement the REST services
		from("direct:shipAddress")
			.log("Retrieving shipping address for order with id ${header.id}")
			.to("sql:select * from bookstore.Address where id IN ( "
					+ "select ship_addr_id from bookstore.Customer where id IN ( "
					+ "select cust_id from bookstore.order_ where id = :#id ))"
					+ "?dataSource=mysqlDataSource&outputType=SelectOne"
					+ "&outputClass=com.redhat.training.jb421.model.Address")
					.marshal().json(JsonLibrary.Jackson);

		from("direct:bookTitles")
			.log("Retrieving a list of book titles included in order with id ${header.id}")
			.to("sql:select title from bookstore.CatalogItem where id IN ( "
					+ "select item_id from bookstore.OrderItem where order_id = :#id)"
					+ "?dataSource=mysqlDataSource&outputType=SelectList")
					.marshal().json(JsonLibrary.Jackson);
	}
}
