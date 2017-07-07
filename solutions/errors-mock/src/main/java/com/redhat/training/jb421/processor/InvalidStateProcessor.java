package com.redhat.training.jb421.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.redhat.training.jb421.exception.InvalidStateException;

public class InvalidStateProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		throw new InvalidStateException();
	}

}
