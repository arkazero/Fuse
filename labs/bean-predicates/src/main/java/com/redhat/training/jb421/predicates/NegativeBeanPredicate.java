package com.redhat.training.jb421.predicates;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;

import javax.inject.Named;

import org.apache.camel.language.XPath;

//@Named("negativeBean")
public class NegativeBeanPredicate {
	
	public boolean isNegative(@XPath("/order/totalInvoice") String value){
		if(new BigDecimal(value).compareTo(ZERO) < 0){
			return true;	
		}else{
			return false;
		}
		
	}

}
