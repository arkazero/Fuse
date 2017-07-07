package com.redhat.training.beans;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PublisherAggregate implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		Document pub = oldExchange.getIn().getBody(org.w3c.dom.Document.class);
		Document books = newExchange.getIn().getBody(org.w3c.dom.Document.class);
		
		try {
			Document aggregate = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element top = aggregate.createElement("PublisherBookInfo");
			Node node1 = aggregate.importNode(pub.getDocumentElement(), true);
			top.appendChild(node1);
			Node node2 = aggregate.importNode(books.getDocumentElement(), true);
			top.appendChild(node2);
			aggregate.appendChild(top);
			newExchange.getIn().setBody(aggregate);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return newExchange;
	}

}
