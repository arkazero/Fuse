package com.redhat.training.jb421;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import com.redhat.training.jb421.model.Order;
import com.redhat.training.jb421.model.OrderItem;

public class VendorLookupAggregationStrategy implements AggregationStrategy{

	@SuppressWarnings("unchecked")
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange databaseResult){
		
		Order originalBody = oldExchange.getIn().getBody(Order.class);
		ArrayList<HashMap<String,Object>> dbResult = databaseResult.getIn().getBody(ArrayList.class);
		
		// match the results from the database to their respective items
		for(OrderItem item: originalBody.getOrderItems()){
			
			for(HashMap<String,Object> row: dbResult){
				int rowId = (int) row.get("id");
				if(rowId == item.getCatalogItem().getId()){
					//set the vendor id and sku from the database results
					item.setVendorId((Integer) row.get("vendor_id"));
					item.setSku((String)row.get("sku"));
				}
			}
			
		}
				
		return oldExchange;
	}

}
