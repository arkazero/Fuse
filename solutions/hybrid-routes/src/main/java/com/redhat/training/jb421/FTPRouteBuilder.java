package com.redhat.training.jb421;

import org.apache.camel.builder.RouteBuilder;

public class FTPRouteBuilder extends RouteBuilder {

	private static String FTP_SERVER = "ftp://{{server.hostname}}:{{server.port}}";
	private static String FTP_OPTIONS = "?username={{server.username}}&password={{server.password}}&delete=true";
	//private static String DIRECT = "direct:header";
	private static String DIRECT = "direct:wrap";
	
	public static String SRC_FOLDER1 = "/abc";
	public static String SRC_FOLDER2 = "/orly";
	public static String SRC_FOLDER3 = "/namming";

	@Override
	public void configure() throws Exception {
		from(FTP_SERVER + SRC_FOLDER1 + FTP_OPTIONS)
		.to(DIRECT);
		from(FTP_SERVER + SRC_FOLDER2 + FTP_OPTIONS)
		.to(DIRECT);
		from(FTP_SERVER + SRC_FOLDER3 + FTP_OPTIONS)
		.to(DIRECT);
	}

}
