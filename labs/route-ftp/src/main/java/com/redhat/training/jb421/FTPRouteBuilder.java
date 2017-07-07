package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

public class FTPRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("file:orders?include=order.*xml")
		//add log DSL here
		.process(new ExchangePrinter())
		.to("file:orders?fileName=${header.CamelFileHost}&fileExist=Append");
	}

}
