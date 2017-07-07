package com.redhat.training.jb421;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import com.redhat.training.jb421.model.OrderItem;

public class DBLookupAggregationStrategy implements AggregationStrategy{

	@SuppressWarnings("unchecked")
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange databaseResult){

		OrderItem originalBody = oldExchange.getIn().getBody(OrderItem.class);
		ArrayList<HashMap<String,Object>> dbResult = databaseResult.getIn().getBody(ArrayList.class);

		for(HashMap<String,Object> row: dbResult){
			int rowId = (int) row.get("id");
			int orderId = Integer.parseInt(oldExchange.getIn().getHeader("order_id",String.class));
					

			if(rowId == orderId){
				originalBody.setCustomerId((Integer) row.get("customer_id"));
			}

			if(rowId == originalBody.getCatalogItem().getId()){
				//set the vendor id and sku from the database results
				originalBody.setVendorId((Integer) row.get("vendor_id"));
				originalBody.setSku((String)row.get("sku"));
			}
		}

		return oldExchange;
	}

}
