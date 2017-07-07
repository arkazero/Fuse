package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class OrderRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// setup the REST web service using the resource class and the simple binding style
		from("cxfrs:http://localhost:8080?resourceClasses=com.redhat.training.jb421.RestOrderService&"
				+ "bindingStyle=SimpleConsumer&provider=jsonProvider")
				// call the route based on the operation invoked on the REST web service
				.toD("direct:${header.operationName}");

		// routes that implement the REST services
		from("direct:createOrder")
			.log("Creating new order with id ${header.id}")
			.setHeader("orderDate").groovy("new Date()")
			//TODO add sql component to insert new order
			.to("");

		from("direct:updateOrder")
			.log("Updating order with id ${header.id} to add order item ${body}")
			.marshal().json(JsonLibrary.Jackson)
			.bean(new JSONPathOrderItemProcessor())
			.to("sql:insert into bookstore.OrderItem (id, order_id, quantity, extPrice) values "
					+ "(:#id, :#orderId, :#quantity, :#extPrice)?dataSource=mysqlDataSource");

		from("direct:cancelOrder")
			.log("Canceling order with id ${header.id}")
			.to("sql:delete from bookstore.OrderItem where order_id = (#)?dataSource=mysqlDataSource")
			.to("sql:delete from bookstore.order_ where id = (#)?dataSource=mysqlDataSource");
	}
}
