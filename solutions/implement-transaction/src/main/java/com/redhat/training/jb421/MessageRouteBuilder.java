package com.redhat.training.jb421;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import java.sql.SQLException;

public class MessageRouteBuilder extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {

		//TODO develop transaction rollback
		onException(SQLException.class)
		.handled(true)
		.markRollbackOnly();
		
        restConfiguration()
            .component("spark-rest").port(8080)
            .contextPath("/restService")
            .apiContextPath("/api-doc")
            .apiProperty("api.title", "Message REST Service API").apiProperty("api.version", "1");

        rest("/messages")
            .post().type(String.class)
                .to("direct:createMessage");
		
        from("direct:createMessage")
			.log("Creating new message: ${body}")
			.to("jms:queue:messages");
			
        	from("jms:queue:messages")
        	.routeId("MessageDB")
        	//TODO Add transaction
        	.transacted()
        	.bean(MessageProcessor.class)
			.log("Queue processed: ${body}")
        	.to("sql:insert into message (message) values"
					+ " (#)?dataSource=atomikosDataSource");
			
	}
}
