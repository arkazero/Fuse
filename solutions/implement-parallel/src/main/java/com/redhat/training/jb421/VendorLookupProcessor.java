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
 * be used by the jdbc module to find the sku and vendor_id for a CatalogItem with
 * a given id.  The query once formed is set as the body on the Exchange.
 *
 */
public class VendorLookupProcessor implements Processor{

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void process(Exchange exchange) {

		Message existing = exchange.getIn();

		OrderItem incomingOrderItem = existing.getBody(OrderItem.class);

		StringBuilder query = new StringBuilder("select sku,vendor_id,id "
				+ "from CatalogItem where id =");

	  query.append(incomingOrderItem.getCatalogItem().getId());

		query.append(";");

//		log.info("Query: "+ query.toString());

		existing.setBody(query.toString());
	}

}
