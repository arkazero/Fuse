package com.redhat.training.jb421;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.spring.Main;


// This class is meant to be run by mvn camel:run, but i can also be run stand-alone

public class TestClient {
	
	static Main main;

	public static void main(String[] args) throws Exception {
		main = new Main();
        main.setApplicationContextUri("META-INF/spring/camel-context.xml");
        main.start();
        sendOrders(main.getCamelContexts().get(0));
	}

	private static String DATA_FOLDER = "../../data/orders";
	private static String URI = "activemq:queue:orders";

	private static void sendOrders(CamelContext context) throws IOException {
		ProducerTemplate template = context.createProducerTemplate();
		File dataFolder = new File(DATA_FOLDER);
		for (File file: dataFolder.listFiles()) {
			String xml = new String(Files.readAllBytes(Paths.get(file.toURI())));
			template.sendBody(URI, xml);
		}
	}
	
}
