package com.redhat.training.beans;

import java.math.BigDecimal;

import org.apache.camel.language.XPath;

public class DestinationBean {

	private final static BigDecimal THRESHHOLD = new BigDecimal(100.00);
	
	public String[] recipients(@XPath(value = "/order/extendedAmount/text()") String amount) {
		if (new BigDecimal(amount).compareTo(THRESHHOLD) == 1)
			return new String[] {"activemq:queue:largeOrders?jmsMessageType=Text"};
		else
			return new String[] {"activemq:queue:smallOrders?jmsMessageType=Text"};
	}
}
