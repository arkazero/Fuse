package com.redhat.training.jb421;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.xml.XPathBuilder;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class DestinationProcessor implements Processor {
	
	final private static Logger log = LoggerFactory.getLogger(DestinationProcessor.class);

	final private static String XPATH_VENDOR_NAME = "/order/orderItems/orderItem/orderItemPublisherName/text()";
	
	public void process(Exchange exchange) {
		log.info("Determining for vendor queue ...");
		String vendor = XPathBuilder.xpath(XPATH_VENDOR_NAME).evaluate(exchange, String.class);
		log.info("vendor: " + vendor);
		String queue = "";
		if ("ABC Company".equals(vendor))
			queue = "abc";
		else if ("ORly".equals(vendor))
			queue = "orly";
		else if ("Namming".equals(vendor))
			queue = "namming";
		log.info("Adding header queueName: " + queue);
		//TODO set the queueName header
	}

}
