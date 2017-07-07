package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.fixed.BindyFixedLengthDataFormat;

import com.redhat.training.jb421.model.Order;

public class AggregateRouteBuilder extends RouteBuilder {
	
	public static String SRC_FOLDER = "/home/student/jb421/labs/aggregator-pattern/orders/incoming";
	
	public static String OUTPUT_FOLDER = "/home/student/jb421/labs/aggregator-pattern/orders/outgoing";
	
	BindyFixedLengthDataFormat bindy = new BindyFixedLengthDataFormat(Order.class);
	
	@Override
	public void configure() throws Exception {
		from("file://"+SRC_FOLDER)
		//TODO split fixed length data by new lines here and use streaming
		.unmarshal(bindy)
		//TODO aggregate into batches of 25 orders here using the ArrayListAggregationStrategy provided
		
		//TODO use the BatchXMLProcessor to marshal the results w/ JAXB and add a <batch> tag around the results

		//TODO add wire tap to direct:orderLogger route
		
		.to("file://"+OUTPUT_FOLDER+"?fileName=output.xml&fileExist=Append","mock:result");
		
		from("direct:orderLogger")
		//TODO add split using tokenizeXML here to split up individual orders and log them
		
		
		.log("${body}");
	}

}