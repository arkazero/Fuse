package com.redhat.training.jb421.beans;

import java.util.Map;

import javax.inject.Named;

import org.apache.camel.Headers;
import org.apache.camel.language.XPath;

@Named
public class DynamicRouterBean {
	public String processDynamicRouterDestination(@XPath(value="/order/orderItems/orderItem/catalogItem/category/text()") String type, @Headers Map<String, Object> headers) {
		Integer processed = (Integer) headers.get("processTimes");
		String destination=null;
		if (processed == null) {
			headers.put("processTimes", 0);
			if("comics".equals(type)){
				destination = "ftp://infrastructure.lab.example.com?username=ftpuser1&password=w0rk1n";
			}else if("book".equals(type)){
				destination = "jms:books"; 
			}else{
				destination = "file://others";
			}
			headers.put("destination", destination);
		}
		return destination;
	}

}
