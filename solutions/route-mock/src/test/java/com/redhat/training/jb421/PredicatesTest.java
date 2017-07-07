package com.redhat.training.jb421;

import static com.redhat.training.jb421.predicates.Predicates.bodyAndHeaderNotNull;
import static org.apache.camel.builder.ExchangeBuilder.anExchange;

import org.apache.camel.Exchange;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.redhat.training.domain.Order;

public class PredicatesTest extends CamelSpringTestSupport {


	//Override the createApplicationContext
	@Override
	protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("META-INF/spring/bundle-context.xml");
	}

	
	@Test
	public void testNullHeaderPredicates() {
		//Evaluate if the bodyAndHeaderNotNull predicate is working
		Exchange exchange = anExchange(context).build();
		Order order = null;
		exchange.getIn().setBody(order);
		exchange.getIn().setHeader("test", null);;
		assertPredicateDoesNotMatch(bodyAndHeaderNotNull(), exchange);
	}
	

	
	
	
	
}
