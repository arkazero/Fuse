package com.redhat.training.jb421;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class PartnerInventoryUpdateProcessor implements Processor{

	@Override
	public void process(Exchange arg0) throws Exception {
		Thread.sleep(20);
	}



}
