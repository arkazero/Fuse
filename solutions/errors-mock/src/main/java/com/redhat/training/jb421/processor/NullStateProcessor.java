package com.redhat.training.jb421.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.redhat.training.jb421.exception.NullStateException;

public class NullStateProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		throw new NullStateException();
	}
	

}
