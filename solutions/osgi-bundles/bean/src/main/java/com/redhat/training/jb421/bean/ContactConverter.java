package com.redhat.training.jb421.bean;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ContactConverter {
	
	final private static Logger log = LoggerFactory.getLogger(ContactConverter.class);

	public static String toXml(String contactLine) {
	    String[] split = contactLine.trim().split("\\s+");
	    String phone = split[0];
	    log.info("phone: " + phone);
	    String name = split[1];
	    for (int i = 2; i < split.length; i++)
	    	name += " " + split[i];
	    log.info("name: " + name);
	    return "<contact>\n"
	    	+ "<name>" + name + "</name>\n"
	    	+ "<phone>" + phone + "</phone>\n"
	    	+ "</contact>\n";
	}

}

