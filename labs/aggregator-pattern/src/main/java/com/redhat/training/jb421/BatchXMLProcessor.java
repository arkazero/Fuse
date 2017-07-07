package com.redhat.training.jb421;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.redhat.training.jb421.model.Order;

public class BatchXMLProcessor implements Processor {

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		
		// Load JAXB Context for Order
		JAXBContext jaxbContext = JAXBContext.newInstance(Order.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		// This option prevents JAXB from including the XML header
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

		// Buffer to hold batch XML string
		StringBuilder batchXML = new StringBuilder();

		// Create opening tag
		batchXML.append("<batch>");

		List<Order> orderBatch = exchange.getIn().getBody(List.class);
		for (Order order : orderBatch) {
			StringWriter sw = new StringWriter();
			marshaller.marshal(order, sw);
			String orderXML = sw.toString();
			sw.close();
			batchXML.append(orderXML);
		}

		// Create closing tag
		batchXML.append("</batch>");

		// Set the result as the new exchange body
		exchange.getIn().setBody(batchXML.toString());

	}

}
