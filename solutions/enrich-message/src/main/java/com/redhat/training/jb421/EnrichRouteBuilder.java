package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.model.dataformat.XmlJsonDataFormat;

public class EnrichRouteBuilder extends RouteBuilder {

	public static String OUTPUT_FOLDER = "/home/student/jb421/solutions/enrich-message/orders/outgoing";
	public static String ERROR_FOLDER = "/home/student/jb421/solutions/enrich-message/orders/error";

	@Override
	public void configure() throws Exception {

		// data format to convert JSON to XML
		XmlJsonDataFormat xmlJsonDataFormat = new XmlJsonDataFormat();
		xmlJsonDataFormat.setRootName("order");
		xmlJsonDataFormat.setElementName("orderItem");
		
		// data format to unmarshal XML into model classes
		JaxbDataFormat jaxbDataFormat = new JaxbDataFormat("com.redhat.training.jb421.model");

		//TODO add onException to handle http 404 errors
		onException(HttpOperationFailedException.class)
			.handled(true);

		//TODO add timer component to poll every 30 seconds
		from("timer:orderTimer?period=30s")
		//TODO add http call to http://localhost:8080/bookstore/rest/order/fulfillOrder to get JSON order data
			.to("http://localhost:8080/bookstore/rest/order/fulfillOrder")
			//TODO convert the JSON data to XML
			.unmarshal(xmlJsonDataFormat)
			.log("${body}")
			.unmarshal(jaxbDataFormat)
			//TODO add enrich to direct vendor lookup jdbc route here and use the VendorLookupAggregationStrategy provided
			.enrich("direct:vendorLookupJDBC", new VendorLookupAggregationStrategy())
			.to("file://"+OUTPUT_FOLDER+"?fileName=output${date:now:yyyy_MM_dd_hh_mm_ss}.xml");

		//TODO add direct vendor lookup jdbc route
		from("direct:vendorLookupJDBC")
			.doTry()
				.process(new VendorLookupProcessor())
				.to("jdbc://mysqlDataSource")
			.doCatch(Exception.class)
				.log("Exception in database lookup! ${body}")
				.to("file://"+ERROR_FOLDER+"?fileName=output${date:now:yyyy_MM_dd_hh_mm_ss}.xml")
			.end();
	}

}
