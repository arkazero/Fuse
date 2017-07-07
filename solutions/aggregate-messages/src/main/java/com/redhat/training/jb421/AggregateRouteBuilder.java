package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AggregateRouteBuilder extends RouteBuilder {
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	public static long BATCH_COMPLETION_INTERVAL = 5000;
	
	@Override
	public void configure() throws Exception {
				
		from("activemq:queue:orderInput?username=admin&password=admin")
		.marshal().jaxb()
		//TODO add aggregate by header("orderType") here using OrderBatchAggregationStrategy()
		.aggregate(header("orderType"),new OrderBatchAggregationStrategy())
		.completionInterval(BATCH_COMPLETION_INTERVAL)
		//TODO add xslt using provided style sheet for updating extPrice and price to include the $ symbol
		.to("xslt:CurrencySymbol.xsl")
		.log("${body}")
		.to("mock:fulfillmentSystem");
		
	}

}



