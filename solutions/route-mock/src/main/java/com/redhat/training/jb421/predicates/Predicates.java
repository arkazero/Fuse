package com.redhat.training.jb421.predicates;

import static org.apache.camel.builder.ExpressionBuilder.bodyExpression;
import static org.apache.camel.builder.ExpressionBuilder.headerExpression;
import static org.apache.camel.builder.PredicateBuilder.and;
import static org.apache.camel.builder.PredicateBuilder.isNotNull;

import org.apache.camel.Predicate;

public class Predicates {
	
	public static Predicate bodyAndHeaderNotNull() {
		return and(isNotNull(bodyExpression()),isNotNull(headerExpression("test")));
	}
	

}
