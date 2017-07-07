package com.redhat.training.jb421;

import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.redhat.training.jb421.beans.OrderTotalBean;
import com.redhat.training.jb421.model.Address;
import com.redhat.training.jb421.model.OrderItem;

public class OrderRouteBuilder extends RouteBuilder {
	
	private static final String PARAM_DESCRIPTION = "The ID of the order";

	@Override
	public void configure() throws Exception {
				
		onException(NonUniqueResultException.class)
			.handled(true)
			.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
			.setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
			.setBody().constant("Data error finding order with ID!");
			
		
		// configure rest-dsl
        restConfiguration()
           	// to use spark-rest component and run on port 8080
            .component("spark-rest").port(8080)
            .bindingMode(RestBindingMode.json)
            .apiContextPath("/api-doc")
            .apiProperty("api.title", "Order REST Service API").apiProperty("api.version", "1");

        // rest services under the orders context-path
        rest("/orders")
            .get("/shipAddress/{id}").outType(Address.class)
            	.param().name("id").type(RestParamType.path).description(PARAM_DESCRIPTION).endParam()
            	.description("Get the shipping address for an order by it's ID")
                .to("direct:shipAddress")
            .get("/orderTotal/{id}").outType(Double.class)
            	.param().name("id").type(RestParamType.path).description(PARAM_DESCRIPTION).endParam()
            	.description("Get the total cost for an order by it's ID")
                .to("direct:orderTotal")
            .get("/itemList/{id}").outType(new TypeReference<Set<OrderItem>>(){}.getClass())
            	.param().name("id").type(RestParamType.path).description(PARAM_DESCRIPTION).endParam()
            	.description("Get the list of OrderItems for an order by it's ID")
                .to("direct:itemList");
		
		// routes that implement the REST services
		from("direct:shipAddress")
			.log("Retrieving shipping address for order with id ${header.id}")
			.to("sql:select * from bookstore.Address where id IN ( select ship_addr_id from bookstore.Customer where id IN ( select cust_id from bookstore.order_ where id = :#id ))"
					+ "?dataSource=mysqlDataSource&outputType=SelectOne"
					+ "&outputClass=com.redhat.training.jb421.model.Address");
		
		from("direct:orderTotal")
			.log("Retrieving order total for order with id ${header.id}")
			.to("sql:select * from bookstore.OrderItem where order_id = :#id"
					+ "?dataSource=mysqlDataSource&outputType=SelectList"
					+ "&outputClass=com.redhat.training.jb421.model.OrderItem")
			.bean(OrderTotalBean.class, "getOrderTotal(${body})");
		
		from("direct:itemList")
			.log("Retrieving order items for order with id ${header.id}")
			.to("sql:select * from bookstore.OrderItem where order_id = :#id"
					+ "?dataSource=mysqlDataSource&outputType=SelectList"
					+ "&outputClass=com.redhat.training.jb421.model.OrderItem");
	}
}
