package com.redhat.training.jb421;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PredicatesTest extends CamelSpringTestSupport {


	//TODO Override the createApplicationContext
	@Override
	protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("META-INF/spring/bundle-context.xml");
	}

	
	@Test
	public void testNullHeaderPredicates() {
		//TODO Evaluate if the bodyAndHeaderNotNull predicate is working
		fail("not implemented yet");
	}
	
	
	
}
