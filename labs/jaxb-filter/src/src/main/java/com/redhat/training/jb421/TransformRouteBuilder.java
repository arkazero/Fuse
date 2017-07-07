package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.XmlJsonDataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformRouteBuilder extends RouteBuilder {
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	XmlJsonDataFormat xmlJson = new XmlJsonDataFormat();
	
	@Override
	public void configure() throws Exception {
				
		from("activemq:queue:orderInput?username=admin&password=admin")
		.marshal().jaxb()
		.log("XML Body: ${body}")
		.marshal(xmlJson)
		.log("JSON Body: ${body}")
		.filter().jsonpath("$[?(@.delivered !='true')]")
		.wireTap("direct:jsonOrderLog")
		.to("mock:fufillmentSystem");
		
		from("direct:jsonOrderLog")
		.startupOrder(1)
		.log("Order recieved: ${body}")
		.to("mock:orderLog");
	}

}
