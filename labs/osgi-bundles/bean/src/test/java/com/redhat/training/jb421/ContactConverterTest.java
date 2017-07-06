package com.redhat.training.jb421;

import static org.junit.Assert.*;

import org.junit.Test;

import com.redhat.training.jb421.bean.ContactConverter;


public class ContactConverterTest {

	private static final String contactLine = "1234567890 John Doe";
	private static final String contactXml = "<contact>\n"
		+ "<name>John Doe</name>\n"
		+ "<phone>1234567890</phone>\n"
		+ "</contact>";

	@Test
	public void testToXml() {
	    assertEquals(contactXml.trim(), ContactConverter.toXml(contactLine).trim());
	}
}

