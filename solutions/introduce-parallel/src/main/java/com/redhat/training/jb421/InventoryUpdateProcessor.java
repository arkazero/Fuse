package com.redhat.training.jb421;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class InventoryUpdateProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		//Simulate processing time to enhance benefits of concurrency
		Thread.sleep(20);
		String fileName = exchange.getIn().getHeader(Exchange.FILE_NAME_ONLY,String.class);
		String publisherId = fileName.substring(fileName.lastIndexOf('-')).replaceAll(".csv","");
		exchange.getIn().setHeader("publisherId", publisherId);
	}

}
