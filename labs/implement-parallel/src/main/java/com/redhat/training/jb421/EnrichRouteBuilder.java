package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;


public class EnrichRouteBuilder extends RouteBuilder {

	public static String OUTPUT_FOLDER = "/home/student/jb421/labs/implement-parallel/orders/outgoing";

	@Override
	public void configure() throws Exception {

		from("jpa:com.redhat.training.jb421.model.Order?persistenceUnit=mysql"
				+ "&consumeLockEntity=false&consumer.delay=10000"
				+ "&consumer.namedQuery=getUndeliveredOrders&consumeDelete=false")
		.bean("timerBean","start")
		.setHeader("order_id", simple("${body.getId()}"))
		.wireTap("direct:updateOrder")
//		TODO use parallel processing in the splitter
		.split(simple("${body.getOrderItems()}"))
//			TODO use multicast to replace two serial enrich calls
			.enrich("direct:vendorLookupJDBC", new DBLookupAggregationStrategy())
			.enrich("direct:customerLookupJDBC", new DBLookupAggregationStrategy())
			.setHeader("vendor_id", simple("${body.getVendorId()}"))
			.marshal().jaxb()
			.to("file:"+OUTPUT_FOLDER+"?fileName=item-${in.header.order_id}-"
					+ "${in.header.vendor_id}-${date:now:yyyy_MM_dd_hh_mm_ss_SS}.xml")
			.bean("timerBean","stop")
		.end();

		from("direct:vendorLookupJDBC")
			.delay(20)
			.process(new VendorLookupProcessor())
			.to("jdbc:mysqlDataSource");

		from("direct:customerLookupJDBC")
			.delay(20)
			.process(new CustomerLookupProcessor())
			.to("jdbc:mysqlDataSource");

		from("direct:updateOrder")
			.process(new DeliverOrderProcessor())
			.to("jdbc:mysqlDataSource");
	}

}
