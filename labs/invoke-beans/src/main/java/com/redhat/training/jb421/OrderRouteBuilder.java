package com.redhat.training.jb421;

import javax.jms.Destination;

import org.apache.camel.builder.RouteBuilder;

import com.redhat.training.jb421.beans.BodyTransformBean;
import com.redhat.training.jb421.beans.DestinationBean;

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
		.to("bean://bodyTransformBean?method=replaceNonASCIIWithQuestionMark")
		.to("direct:recipientList");

		//TODO implement the third route
		from("direct:recipientList")
		.routeId("recipientList")
		.split(body().convertToString().tokenize("\n"))
		.setHeader("destination",method(DestinationBean.class,"calculateDestination"))
		.recipientList(header("destination"));

	}

}
