package com.redhat.training.jb421.beans;

public class BodyTransformBean {

	public String replaceCommaWithSemiColon(String parameter){
		return parameter.replace(',', ';');
		
	}
	public String replaceCommaWithColon(String parameter){
		return parameter.replace(',', ':');
	}

}
