package com.redhat.training.jb421;

import org.apache.camel.Converter;

import com.google.gson.Gson;
import com.redhat.training.jb421.model.Order;

@Converter
public class OrderJSONTypeConverter {

	@Converter
	public String convertToJSON(Order order){
		Gson gson = new Gson();
		return gson.toJson(order);
	}
}
