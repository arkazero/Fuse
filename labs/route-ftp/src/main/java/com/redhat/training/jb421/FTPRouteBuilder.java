package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

public class FTPRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("ftp://infrastructure.lab.example.com?username=ftpuser1&password=w0rk1n&delete=true&include=order.*xml")
		//from("file:orders?include=order.*xml")
		//add log DSL here
		.log("New file ${header.CamelFileName} picked up from ${header.CamelFileHost}")
		.process(new ExchangePrinter())
		.to("file:orders?fileName=${header.CamelFileHost}&fileExist=Append");
	}

}
