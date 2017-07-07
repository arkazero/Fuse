package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;

import com.redhat.training.jb421.model.CatalogItem;

public class TransformRouteBuilder extends RouteBuilder {
	
	public static String SRC_FOLDER = "/home/student/jb421/solutions/bindy-mtp/items/incoming";
	
	//TODO add bindy data format
	BindyCsvDataFormat bindy = new BindyCsvDataFormat(CatalogItem.class);
	
	@Override
	public void configure() throws Exception {
		from("file:" + SRC_FOLDER )
		//TODO change EOL char with transform()
		.transform(body().regexReplaceAll("\n", "\r\n"))
		//TODO unmarshal with bindy
		.unmarshal(bindy)
		//TODO wiretap to direct route
		.wireTap("direct:loggingSystem")
		.to("mock:inventorySystem");

		//TODO add direct route
		from("direct:loggingSystem")
		.split().simple("${body}")
		.convertBodyTo(String.class)
		.filter(simple("${body} contains 'new'"))
		.log("${body}")
		.to("mock:newItemsFeed");
		
	}

}