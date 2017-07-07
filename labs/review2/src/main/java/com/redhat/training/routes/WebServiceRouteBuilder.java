package com.redhat.training.routes;

import org.apache.camel.builder.RouteBuilder;

import com.redhat.training.beans.PublisherAggregate;

public class WebServiceRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		//TODO Configure the restComponent
		//TODO Configure the use the spark-rest component
		//TODO Use port 8081

		//TODO Activate a REST endpoint that will receive requests 
		//from /publisher/name
		//TODO Response to an HTTP GET request. Read the parameter 
		//and generate an XML response
		//TODO send to a second route

		// Second route			
			from("direct:get")
				.routeId("NameWSGet")
				// TODO Eliminate all the headers starting with 
				// CamelHttp to avoid confusion 
				// with the bookstore app
				//TODO send header to get information 
				//from the bookstore REST API 
				// TODO Send a request to the bookstore 
				// application /rest/pub/name/ 
				//and send the header field named name
				.choice()
				// TODO Evaluate the response If the response is 204
					//TODO manage with the route direct:error
					//TODO If the response is 200 (success)
					//TODO Enrich the data
				.end();
			
			from("direct:enrich")
				.routeId("Enricher")
				.convertBodyTo(String.class)
				//Read the header from the body and store it to the header named publisher_id
				.setHeader("publisher_id", xpath("/publisher/@id", String.class))
				//TODO Remove any existing header with HTTP protocol information
				//TODO configure the request to GET the remaining information.
				//TODO enrich the existing XML with the publisher information obtained from the bookstore app
			;
			
			//TODO route with error
//			from("direct:error")
//				.routeId("ProcessError")
//			;
			
	}

}
