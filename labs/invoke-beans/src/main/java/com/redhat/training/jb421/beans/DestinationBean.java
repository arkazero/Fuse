package com.redhat.training.jb421.beans;

public class DestinationBean {

	public String calculateDestination(String body){
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
		return value;
	}
}
