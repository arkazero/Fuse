package com.redhat.training.jb421.predicates;

import java.math.BigDecimal;

import javax.inject.Named;

import org.apache.camel.language.XPath;
import static java.math.BigDecimal.*;

//@Named("silverBean")
public class SilverBeanPredicate {
	
	public static final BigDecimal FIFTY = new BigDecimal(50);
	public boolean isSilver(@XPath("/order/totalInvoice") String value){
		BigDecimal totalInvoice = new BigDecimal(value);
		if(totalInvoice.compareTo(ZERO)>=0 && totalInvoice.compareTo(FIFTY) <= 0){
			return true;	
		}else{
			return false;
		}
		
	}
}
