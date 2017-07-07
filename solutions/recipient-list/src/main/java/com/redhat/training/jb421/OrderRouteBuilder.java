package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

import com.redhat.training.jb421.beans.BodyTransformBean;

public class OrderRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		
		
		from("file:origin")
		//TODO Call the method complementExchange from BodyTransformBean
		.bean(BodyTransformBean.class,"replaceCommaWithSemiColon")
		.to("mock:destination"); 

	
		from("file:originSelectMethod")
		//TODO Call the method complementExchange from BodyTransformBean
		.to("bean:bodyTransformBean?method=replaceCommaWithColon")
		.to("mock:destinationSelectMethod");

		from("file:originOrders")
		//TODO Call the recipientListBean
		.bean("destinationBean")
		.recipientList(header("destination")); 
	}

}
