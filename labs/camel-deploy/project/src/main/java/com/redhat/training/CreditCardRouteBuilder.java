package com.redhat.training;

import org.apache.camel.builder.RouteBuilder;

import com.redhat.training.jb421.bean.CreditCardUtils;

public class CreditCardRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		restConfiguration().component("spark-rest").port(8081);

		rest("/cc").get("{creditCardNumber}").produces("text/plain").to("direct:cc");
		
		from("direct:cc").routeId("cc-get")
		.bean(CreditCardUtils.class,"validate(${header.creditCardNumber})");
	}

}
