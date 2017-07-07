package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

public class OrderRouteBuilder extends RouteBuilder {
	
	public static final String CREATE_ORDER_DEST="createOrder";
	public static final String UPDATE_ORDER_DEST="updateOrder";
	public static final String CANCEL_ORDER_DEST="cancelOrder";

	@Override
	public void configure() throws Exception {

		from("restlet:http://0.0.0.0:8080/orders?restletMethods=POST,PUT,DELETE")
			.process(new HttpQueryHeaderProcessor())
			.toD("direct:${header.destination}");

		// routes that implement the REST services
		from("direct:"+CREATE_ORDER_DEST)
			.log("Creating new order with id ${header.id}")
			.setHeader("orderDate").groovy("new Date()")
			.to("sql:insert into bookstore.order_ (id, orderDate) values"
						+ " (:#id, :#orderDate)?dataSource=mysqlDataSource");

		from("direct:"+UPDATE_ORDER_DEST)
			.convertBodyTo(String.class)
			.log("Updating order with id ${header.id} to add order item ${body}")
			.bean(new XPathOrderItemProcessor())
			.to("sql:insert into bookstore.OrderItem (id, order_id, quantity, extPrice) values "
				+ "(:#id, :#orderId, :#quantity, :#extPrice)?dataSource=mysqlDataSource");

		from("direct:"+CANCEL_ORDER_DEST)
			.log("Canceling order with id ${header.id}")
			.to("sql:delete from bookstore.OrderItem where order_id = (:#id)?dataSource=mysqlDataSource")
			.to("sql:delete from bookstore.order_ where id = (:#id)?dataSource=mysqlDataSource");
	}
}
