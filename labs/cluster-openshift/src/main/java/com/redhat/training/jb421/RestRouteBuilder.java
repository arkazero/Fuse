package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

public class RestRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		restConfiguration().component("spark-rest").port(8081);

		rest("/hello").get("{name}").produces("text/xml").to("direct:sayHello");

		from("direct:sayHello").routeId("HelloWS-get")
			.setBody().simple("<message>\n"
			    + "  <greeting>Hello, ${header.name}.</greeting>\n"
			    + "  <server>" + System.getenv("HOSTNAME") + "</server>\n"
			    + "</message>");
	}

}
