package com.redhat.training.jb421;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeliverOrderProcessor implements Processor {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Override
	public void process(Exchange exchange) throws Exception {
		Message existing = exchange.getIn();

		StringBuilder query = new StringBuilder("update order_ set delivered=1 where id =");

		query.append(exchange.getIn().getHeader("order_id",String.class));

		query.append(";");

//		log.info("Query: "+ query.toString());

		existing.setBody(query.toString());
	}

}
