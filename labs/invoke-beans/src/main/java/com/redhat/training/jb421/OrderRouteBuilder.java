package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

import com.redhat.training.jb421.beans.BodyTransformBean;

public class OrderRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("file:origin")
		.routeId("transformComma")
		.bean(BodyTransformBean.class,"replaceCommaWithBacktick")
		.to("direct:transformChars"); 

		//TODO implement second route
		from("direct:transformChars")
		.routeId("transformChars")
		//TODO send to the bean component
		.to("direct:recipientList");

		//TODO implement the third route

	}

}
