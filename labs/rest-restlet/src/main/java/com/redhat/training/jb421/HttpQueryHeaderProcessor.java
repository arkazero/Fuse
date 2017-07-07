package com.redhat.training.jb421;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.util.URISupport;

public class HttpQueryHeaderProcessor implements Processor{
	
	public static final String ID_KEY="id";

	@Override
	public void process(Exchange exchange) throws Exception {
		String query = exchange.getIn().getHeader(Exchange.HTTP_QUERY,String.class);
		String method = exchange.getIn().getHeader(Exchange.HTTP_METHOD,String.class);
		String destination = "";
		
		if(method.equalsIgnoreCase("POST")){
			destination = OrderRouteBuilder.CREATE_ORDER_DEST; 
		}else if(method.equalsIgnoreCase("PUT")){
			destination = OrderRouteBuilder.UPDATE_ORDER_DEST;
		}else if(method.equalsIgnoreCase("DELETE")){
			destination = OrderRouteBuilder.CANCEL_ORDER_DEST;
		}else{
			throw new UnsupportedOperationException("Only HTTP POST, HTTP PUT, and HTTP DELETE are currently supported!");
		}
		exchange.getIn().setHeader("destination", destination);
		
		
		Map<String,Object> queryParams = URISupport.parseQuery(query);
		if(queryParams.containsKey(ID_KEY)){
			exchange.getIn().setHeader(ID_KEY, queryParams.get(ID_KEY));
		}else{
			throw new UnsupportedOperationException("A query parameter containing the 'id' is required!");
		}
		
	}

}
