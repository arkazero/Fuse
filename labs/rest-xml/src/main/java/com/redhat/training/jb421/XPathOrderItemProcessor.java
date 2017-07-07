package com.redhat.training.jb421;

import java.math.BigDecimal;

import org.apache.camel.Exchange;
import org.apache.camel.language.XPath;

public class XPathOrderItemProcessor{

	public void process(@XPath("orderItem/id") Integer id,
            @XPath("orderItem/quantity") Integer quantity,
            @XPath("orderItem/extPrice") String price, Exchange exchange) throws Exception {
        exchange.getIn().setHeader("orderId", exchange.getIn().getHeader("id"));
		exchange.getIn().setHeader("id", id);
        exchange.getIn().setHeader("quantity", quantity);
        exchange.getIn().setHeader("extPrice", new BigDecimal(price));
    }

}
