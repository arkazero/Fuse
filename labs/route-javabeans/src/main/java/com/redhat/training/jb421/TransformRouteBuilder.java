package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

public class TransformRouteBuilder extends RouteBuilder {
	
	public static String OUTPUT_FOLDER = "orders/outgoing";

	public static Long BATCH_TIMEOUT = 10000L;
	
	
	@Override
	public void configure() throws Exception {
		from("jpa:com.redhat.training.jb421.model.Order?persistenceUnit=mysql"
				+ "&consumeDelete=false"
				+ "&consumer.namedQuery=getUndeliveredOrders"
				+ "&consumer.delay="+BATCH_TIMEOUT
				+ "&consumeLockEntity=true")
			.wireTap("direct:updateOrder")
			.marshal().jaxb()
			.split(xpath("order/orderItems/orderItem"))
			.aggregate(xpath("orderItem/catalogItem/id"),new ReservationAggregationStrategy())
				.completionInterval(BATCH_TIMEOUT)
				.completeAllOnStop()
			.log("${body}")
			.setHeader("CatalogItemId",xpath("/reservation/catalogItemId/text()"))
			.to("file:"+OUTPUT_FOLDER+"?fileName=${header.CatalogItemId}/"
					+ "reservation-${date:now:yyyy-MM-dd_HH-mm-ss}.xml");
		
		from("direct:updateOrder")
			.process("deliverOrder")
			.to("jpa:com.redhat.training.jb421.model.Order?persistenceUnit=mysql");
		
	}

}
