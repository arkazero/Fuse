package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

public class TransformRouteBuilder extends RouteBuilder {
	
	public static String OUTPUT_FOLDER = "/home/student/jb421/solutions/transform-data/orders/outgoing";

	public static Long BATCH_TIMEOUT = 10000L;
	
	
	@Override
	public void configure() throws Exception {
		//TODO add jpa consumer
		from("jpa:com.redhat.training.jb421.model.Order?persistenceUnit=mysql"
				+ "&consumeDelete=false"
				+ "&consumer.namedQuery=getUndeliveredOrders"
				+ "&consumer.delay="+BATCH_TIMEOUT
				+ "&consumeLockEntity=false")
			//TODO add wire tap to second route
			.wireTap("direct:updateOrder")
			//TODO marshal order to XML with JAXB
			.marshal().jaxb()
			//TODO split the order into individual order items
			.split(xpath("order/orderItems/orderItem"))
			//TODO aggregate the order items based on their catalog item id
			.aggregate(xpath("orderItem/catalogItem/id"),new ReservationAggregationStrategy())
				.completionInterval(BATCH_TIMEOUT)
				.completeAllOnStop()
			//TODO log the reservation XML to the console
			.log("${body}")
			.setHeader("CatalogItemId",xpath("/reservation/catalogItemId/text()"))
			//TODO add file producer
			.to("file:"+OUTPUT_FOLDER+"?fileName=${header.CatalogItemId}/"
					+ "reservation-${date:now:yyyy-MM-dd_HH-mm-ss}.xml");
		//TODO add second route to update order in the database
		from("direct:updateOrder")
			.process(new DeliverOrderProcessor())
			.to("jpa:com.redhat.training.jb421.model.Order?persistenceUnit=mysql");
			
	}

}
