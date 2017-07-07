package com.redhat.training.jb421;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;

import com.redhat.training.jb421.model.InventoryRequest;

public class ConcurrentRouteBuilder extends RouteBuilder {

	public static String OUTPUT_FOLDER = "/home/student/jb421/solutions/introduce-concurrency/orders/outgoing";

	@Override
	public void configure() throws Exception {

		from("jpa:com.redhat.training.jb421.model.Order?persistenceUnit=mysql"
				+ "&consumer.namedQuery=getUndeliveredOrders")
			.bean("timerBean","start")
			.split(simple("${body.getOrderItems()}"))
				.to("seda:inventoryProcessor")
			.end();

 		from("seda:inventoryProcessor?concurrentConsumers=20")
		 		.process(new InventoryRequestProcessor())
				.marshal().bindy(BindyType.Csv,InventoryRequest.class)
				.to("file:"+OUTPUT_FOLDER+"?fileName=${header.publisherId}/"
						+ "inventory-reservation-${date:now:yyyy-MM-dd}.csv&fileExist=Append")
		 		.bean("timerBean","stop");
	}

}
