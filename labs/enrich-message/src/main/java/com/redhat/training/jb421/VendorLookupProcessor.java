package com.redhat.training.jb421;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.training.jb421.model.Order;
import com.redhat.training.jb421.model.OrderItem;

/**
 * Processor used to convert an incoming Order into a sql query to 
 * be used by the jdbc module to find the sku and vendor_id for a CatalogItem with
 * a given id.  The query once formed is set as the body on the Exchange.
 *
 */
public class VendorLookupProcessor implements Processor{
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		//Reading the message
		Message existing = exchange.getIn();
		//Convert the body to an order.		
		Order incomingOrder = existing.getBody(Order.class);
		
		//Build a SQL query to obtain the CatalogItems.
		StringBuilder query = new StringBuilder("select sku,vendor_id,id "
				+ "from CatalogItem where id in (");

		for(OrderItem order: incomingOrder.getOrderItems()){
			query.append(order.getCatalogItem().getId());
			query.append(",");
		}
		
		query.delete(query.lastIndexOf(","), query.length());
		query.append(");");
		
		log.info("Query: "+ query.toString());
		//append the output to the body.
		existing.setBody(query.toString());
	}

}
