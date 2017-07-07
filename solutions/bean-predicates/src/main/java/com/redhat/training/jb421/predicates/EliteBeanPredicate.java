package com.redhat.training.jb421.predicates;

import java.math.BigDecimal;

import javax.inject.Named;

import org.apache.camel.language.XPath;
import static com.redhat.training.jb421.predicates.GoldBeanPredicate.HUNDRED;

@Named("eliteBean")
public class EliteBeanPredicate {
	
	public boolean isElite(@XPath("/order/totalInvoice") String value){
		if(new BigDecimal(value).compareTo(HUNDRED) > 0){
			return true;	
		}else{
			return false;
		}
		
	}

}
