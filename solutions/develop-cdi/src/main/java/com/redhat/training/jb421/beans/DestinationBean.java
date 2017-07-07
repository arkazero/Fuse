package com.redhat.training.jb421.beans;

import java.util.Map;

import javax.inject.Named;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.apache.camel.Headers;

@Named
public class DestinationBean {

	@Handler
	public void processDestination(@Body String body, @Headers Map<String, Object> headers) {
		String[] fields = body.split(",");
		int value = Integer.parseInt(fields[6]);
		String destination;
		switch (value) {
		case 1:
			destination = "ftp://infrastructure.lab.example.com?username=ftpuser1&password=w0rk1n";
			break;
		case 2:
			destination = "ftp://infrastructure.lab.example.com?username=ftpuser2&password=w0rk0ut";
			break;
		default:
			destination = "file://others";

		}
		headers.put("destination", destination);
	}

	public String processDynamicRouterDestination(@Body String body, @Headers Map<String, Object> headers) {
		Integer processed = (Integer) headers.get("processTimes");
		String destination=null;
		if (processed == null) {
			headers.put("processTimes", 0);
			String[] fields = body.split(",");
			int value = Integer.parseInt(fields[6]);
			switch (value) {
			case 1:
				destination = "ftp://infrastructure.lab.example.com?username=ftpuser1&password=w0rk1n";
				break;
			case 2:
				destination = "ftp://infrastructure.lab.example.com?username=ftpuser2&password=w0rk0ut";
				break;
			default:
				destination = "file:others";
			}
		}
		return destination;
	}
}
