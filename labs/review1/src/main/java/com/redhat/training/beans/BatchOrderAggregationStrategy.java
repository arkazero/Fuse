package com.redhat.training.beans;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class BatchOrderAggregationStrategy implements AggregationStrategy {
	
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		//TODO if this is the  first aggregate execution, then
			//TODO create a new batch instance with the value obtained from batchNumber (Header attribute)
			//TODO add the order obtained from the body
			//TODO Add the order to the batch
		//TODO otherwise get the batch stored in the oldExchange
		//TODO get the order from the newExchange
		//TODO add the order to the Batch instance
		//TODO store the batch to the oldExchange body
		//TODO return the oldExchange
		return null;
	}


}
