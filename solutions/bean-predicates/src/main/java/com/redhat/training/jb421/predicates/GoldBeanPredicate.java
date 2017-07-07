package com.redhat.training.jb421.predicates;

import java.math.BigDecimal;

import javax.inject.Named;

import org.apache.camel.language.XPath;

import static com.redhat.training.jb421.predicates.SilverBeanPredicate.FIFTY;

@Named("goldBean")
public class GoldBeanPredicate {
	public static final BigDecimal HUNDRED = new BigDecimal(100);
	
	public boolean isGold(@XPath("/order/totalInvoice") String value){
		BigDecimal totalInvoice = new BigDecimal(value);
		if(totalInvoice.compareTo(FIFTY) > 0 && totalInvoice.compareTo(HUNDRED)<=0){
			return true;	
		}else{
			return false;
		}
		
	}

}
