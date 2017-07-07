package com.redhat.training.jb421;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.training.jb421.model.Order;
import com.redhat.training.jb421.model.OrderItem;

/**
 * Processor used to convert an incoming OrderItem into a sql query to
 * be used by the jdbc module to find the customer id for an order with
 * a given id.  The query once formed is set as the body on the Exchange.
 *
 */
public class CustomerLookupProcessor implements Processor{

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void process(Exchange exchange) {

		Message existing = exchange.getIn();

		StringBuilder query = new StringBuilder("select cust_id,id "
				+ "from order_ where id =");

		query.append(exchange.getIn().getHeader("order_id",String.class));

		query.append(";");

//		log.info("Query: "+ query.toString());

		existing.setBody(query.toString());
	}

}
