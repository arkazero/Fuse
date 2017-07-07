package com.redhat.training.jb421;

import java.math.BigDecimal;

import org.apache.camel.Exchange;
import org.apache.camel.jsonpath.JsonPath;

public class JSONPathOrderItemProcessor{

	public void process(@JsonPath("$.id") Integer id,
            @JsonPath("$.quantity") Integer quantity,
            @JsonPath("$.extPrice") String price, Exchange exchange) throws Exception {
        exchange.getIn().setHeader("orderId", exchange.getIn().getHeader("id"));
		exchange.getIn().setHeader("id", id);
        exchange.getIn().setHeader("quantity", quantity);
        exchange.getIn().setHeader("extPrice", new BigDecimal(price));
    }

}
