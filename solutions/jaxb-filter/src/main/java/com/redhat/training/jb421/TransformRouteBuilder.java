package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.XmlJsonDataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformRouteBuilder extends RouteBuilder {
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	//TODO add the XmlJsonDataFormat 
	XmlJsonDataFormat xmlJson = new XmlJsonDataFormat();
	
	@Override
	public void configure() throws Exception {
				
		from("activemq:queue:orderInput?username=admin&password=admin")
		.marshal().jaxb()
		.log("XML Body: ${body}")
		//TODO Marshal JSON
		.marshal(xmlJson)
		.log("JSON Body: ${body}")
		//TODO Filter JSON
		.filter().jsonpath("$[?(@.delivered !='true')]")
		//TODO wire tap
		.wireTap("direct:jsonOrderLog")
		.to("mock:fulfillmentSystem");
		
		from("direct:jsonOrderLog")
		.log("Order received: ${body}")
		.to("mock:orderLog");
	}

}



