package com.redhat.training.jb421;

import javax.persistence.NoResultException;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;

import com.redhat.training.jb421.model.Order;
import com.redhat.training.jb421.model.OrderItem;

public class OrderRouteBuilder extends RouteBuilder {

	private static final String PARAM_DESCRIPTION = "The ID of the order";

	@Override
	public void configure() throws Exception {

		onException(NoResultException.class)
			.handled(true)
			.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
			.setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
			.setBody().constant("No order found with ID!");


				// configure rest-dsl
        restConfiguration()
           	// to use spark-rest component and run on port 8080
            .component("spark-rest").port(8080)
            .bindingMode(RestBindingMode.json)
            //TODO add swagger context path as api-doc
            .apiContextPath("")
            .apiProperty("api.title", "Order REST Service API")
            .apiProperty("api.version", "1");

        // rest services under the orders context-path
        rest("/orders")
            .get("{id}")
            	.param().name("id").type(RestParamType.path).description(PARAM_DESCRIPTION).endParam()
            	.description("Get an existing order by it's ID")
              .to("direct:getOrder")
            .post("{id}").type(Order.class)
            	.param().name("id").type(RestParamType.path).description(PARAM_DESCRIPTION).endParam()
            	.description("Create a new order with a given ID")
              .to("direct:createOrder")
            .put("{id}").type(OrderItem.class)
            	.param().name("id").type(RestParamType.path).description(PARAM_DESCRIPTION).endParam()
            	.description("Update an existing order to add an item")
              .to("direct:updateOrder")
            .delete("{id}").type(Order.class)
            	.param().name("id").type(RestParamType.path).description(PARAM_DESCRIPTION).endParam()
            	.description("Cancel an order and it's items")
              .to("direct:cancelOrder");

		// routes that implement the REST services
		from("direct:getOrder")
			.log("Retrieving order with id ${header.id}")
			.to("sql:select * from bookstore.order_ "
					+ " where id = :#id"
					+ "?dataSource=mysqlDataSource"
					+ "&outputType=SelectOne&outputClass=com.redhat.training.jb421.model.Order"
					+ "&outputHeader=order")
			.to("sql:select * from bookstore.OrderItem "
					+ " where order_id = :#id"
					+ "?dataSource=mysqlDataSource"
					+ "&outputType=SelectList&outputClass=com.redhat.training.jb421.model.OrderItem")
			.process(new OrderItemProcessor());

        from("direct:createOrder")
			.log("Creating new order with id ${header.id}")
			.setHeader("orderDate").groovy("new Date()")
			.to("sql:insert into bookstore.order_ (id, orderDate) values"
					+ " (:#id, :#orderDate)?dataSource=mysqlDataSource");

		from("direct:updateOrder")
			.log("Updating order with id ${header.id} to add order item ${body}")
			.marshal().json(JsonLibrary.Jackson)
			.bean(new JSONPathOrderItemProcessor())
			.to("sql:insert into bookstore.OrderItem (id, order_id, quantity, extPrice) values "
					+ "(:#id, :#orderId, :#quantity, :#extPrice)?dataSource=mysqlDataSource");

		from("direct:cancelOrder")
			.log("Canceling order with id ${header.id}")
			.to("sql:delete from bookstore.OrderItem where order_id = (:#id)?dataSource=mysqlDataSource")
			.to("sql:delete from bookstore.order_ where id = (:#id)?dataSource=mysqlDataSource");
	}
}
