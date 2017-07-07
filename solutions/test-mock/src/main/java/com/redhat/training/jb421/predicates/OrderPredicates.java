package com.redhat.training.jb421.predicates;

import static org.apache.camel.builder.ExpressionBuilder.simpleExpression;
import static org.apache.camel.builder.PredicateBuilder.isGreaterThan;
import static org.apache.camel.builder.PredicateBuilder.regex;

import org.apache.camel.Predicate;

public class OrderPredicates {
	
	public static Predicate validateZipCode() {
		return regex(simpleExpression("body.customer.shippingAddress.postalCode"), "\\d{5}(?:[-s]\\d{4})?$");
	}

	public static Predicate validateNumberOfItems() {
		return isGreaterThan(simpleExpression("body.items.size"),simpleExpression("2"));
	}


}
