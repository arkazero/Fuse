package com.redhat.training.jb421.beans;

public class BodyTransformBean {

	public String replaceCommaWithBacktick(String parameter){
		return parameter.replace(',', '`');
		
	}
	public String replaceNonASCIIWithQuestionMark(String parameter){
		return ((String)parameter).replaceAll("[^A-Za-z0-9\\s,;./`]", "?");
	}

}
