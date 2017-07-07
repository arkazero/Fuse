package com.redhat.training.jb421.beans;

import javax.inject.Named;

import org.apache.camel.language.XPath;


//TODO Annotate
@Named("emptyState")
public class EmptyStateBean {
	public boolean emptyState(@XPath("/order/customer/shippingAddress/state/text()") String state){
		if(state == null || "".equals(state)){
			return true;
		}else{
			return false;
		}
	}

}
