package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

public class FileRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("file://origin")
		// TODO Filter all the order*.xml files with the orderItemPublisherName = ABC Company
		// TODO As a guideline the following XPath will be used: "/order/orderItems/orderItem/orderItemPublisherName/text()='ABC Company'";
		.to("file://destination?fileExist=Append&fileName=output.xml");
	}

}
