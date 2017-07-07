package com.redhat.training.jb421;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.xml.XPathBuilder;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;
import org.slf4j.Logger;

public class TestProcessor implements Processor {
	
	final private static Logger log = LoggerFactory.getLogger(TestProcessor.class);

	final private static String XPATH_TEST = "/order/test";
	
	public void process(Exchange exchange) {
		log.info("Checking for test order ...");
		NodeList test = XPathBuilder.xpath(XPATH_TEST).evaluate(exchange, NodeList.class);
		if (test.getLength() != 0) {
			log.info("Adding skipOrder header");
			//TODO set the skipOrder header
		}
	}

}
