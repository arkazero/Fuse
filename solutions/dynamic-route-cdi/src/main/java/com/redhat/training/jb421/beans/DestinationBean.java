package com.redhat.training.jb421.beans;

import java.util.Map;

import javax.inject.Named;

import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.apache.camel.language.XPath;

@Named
public class DestinationBean {

	@Handler
	public void processDestination(@XPath(value="/order/orderItems/orderItem/catalogItem/category/text()") String type, @Headers Map<String, Object> headers) {
		String destination;
		if("comics".equals(type)){
			destination = "ftp://infrastructure.lab.example.com?username=ftpuser1&password=w0rk1n";
		}else if("book".equals(type)){
			destination = "jms:books"; 
		}else{
			destination = "file://others";
		}
		System.out.println(destination);
		headers.put("destination", destination);
	}

}
