package com.redhat.training.jb421;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import com.redhat.training.jb421.model.Order;
import com.redhat.training.jb421.model.OrderBatch;

public class OrderBatchAggregationStrategy implements AggregationStrategy {

    public OrderBatchAggregationStrategy() {
            super();
    }

    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            Message newIn = newExchange.getIn();
            Order newBody = newIn.getBody(Order.class);
            OrderBatch batch = null;
            if (oldExchange == null) {
                    batch = new OrderBatch();
                    batch.getOrders().add(newBody);
                    batch.setOrderType(newIn.getHeader("orderType",String.class));
                    newIn.setBody(batch);
                    return newExchange;
            } else {
                    Message in = oldExchange.getIn();
                    batch = in.getBody(OrderBatch.class);
                    batch.getOrders().add(newBody);
                    return oldExchange;
            }
    }

}