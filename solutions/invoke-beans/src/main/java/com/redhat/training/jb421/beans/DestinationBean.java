package com.redhat.training.jb421.beans;

public class DestinationBean {

	//TODO Annotate the parameters
	public String calculateDestination(String body){
//		, @Headers Map<String, Object> headers){
//	}
		String[] split = body.split("`");
		char c = split[6].charAt(0);
		String value;
		switch(c){
		case '1':
			value="ftp:infrastructure";
			break;
		case '2':
			value="activemq:comics"; 
			break;
		default:
			value="file:test";
			break;
		}
		
		//store the value in the header field named destination
//		headers.put("destination", value);
//		return headers;
		return value;
	}
}
