package com.redhat.training.jb421;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import java.sql.SQLException;

import com.redhat.training.jb421.model.Message;

public class MessageProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		String body = (String)exchange.getIn().getBody();
		if(body==null | !body.startsWith("orderId=")){
			throw new SQLException();
		}else{
			Message message = new Message();
			message.setMessage(body);
			exchange.getIn().setBody(message);
		}
		
	}

}
