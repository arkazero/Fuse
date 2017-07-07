package com.redhat.training.jb421.beans;

import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Headers;

public class DestinationBean {

	public void processDestination(@Body String body, @Headers Map<String, Object> header){
		String[] split = body.split(",");
		char c = split[6].charAt(0);
		String value;
		switch(c){
		case '1':
			value="mock:ftp:infrastructure";
			break;
		case '2':
			value="mock:jms:comics"; 
			break;
		default:
			value="mock:file:test";
			break;
		}
		header.put("destination", value);
	}
}
