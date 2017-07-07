package com.redhat.training.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import com.redhat.training.beans.PublisherAggregate;

public class WebServiceRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		//TODO Configure the resstComponent
			restConfiguration()
		//TODO Configure the use the spark-rest component
				.component("spark-rest")
		//TODO Use port 8081
					.port(8081)
			;
		//TODO Activate a REST endpoint that will receive requests from /publisher/name
			rest("/publisher/name")
		//TODO Response to an HTTP GET request. Read the parameter and generate an XML response
				.get("{name}").produces("text/xml")
		//TODO send to a second route
					.to("direct:get")
			;
// Second route			
			from("direct:get")
				.routeId("NameWSGet")
		// TODO Eliminate all the headers starting with CamelHttp to avoid confusion with the bookstore app
				.removeHeaders("CamelHttp*")
				//TODO send header to get information from the bookstore REST API 
				.setHeader(Exchange.HTTP_METHOD, constant("GET"))
				// TODO Send a request to the bookstore application /rest/pub/name/ and send the header field named name
				.toD("http://localhost:8080/bookstore/rest/pub/name/${header.name}")
				.choice()
				// TODO Evaluate the response If the response is 204
					.when(header("CamelHttpResponseCode").isEqualTo("204"))
					//TODO manage with the route direct:error
						.to("direct:error")
					//TODO If the response is 200 (success)
					.when(header("CamelHttpResponseCode").isEqualTo("200"))
					//TODO Enrich the data
						.to("direct:enrich")
				.end()
			;
			
			from("direct:enrich")
				.routeId("Enricher")
				.convertBodyTo(String.class)
				//Read the header from the body and store it to the header named publisher_id
				.setHeader("publisher_id", xpath("/publisher/@id", String.class))
				//TODO Remove any existing header with HTTP protocol information
				.removeHeaders("CamelHttp*")
				//TODO configure the request to GET the remaining information.
				.setHeader(Exchange.HTTP_METHOD, constant("GET"))
				//TODO enrich the existing XML with the publisher information obtained from the bookstore app
				.enrich().simple("http://localhost:8080/bookstore/rest/pub/id/${header.publisher_id}/books").aggregationStrategy(new PublisherAggregate())
			;
			
			//TODO route with error
			from("direct:error")
				.routeId("ProcessError")
				.setBody(constant("Publisher not found."))
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant("404"))
			;
			
	}

}
