package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

public class FileRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
	// TODO  The following is the XPath definition used by this lab: /order/orderItems/orderItem[not(contains(orderItemPublisherName,'ABC Company'))]
		from("file://origin")
		.filter(xpath("/order/orderItems/orderItem[not(contains(orderItemPublisherName,'ABC Company'))]"))
		.to("file://destination?fileExist=Append&fileName=output.xml");

	}


}
