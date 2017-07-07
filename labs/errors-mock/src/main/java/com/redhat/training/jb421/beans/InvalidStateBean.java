package com.redhat.training.jb421.beans;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.inject.Named;

import org.apache.camel.language.XPath;
//TODO Annotate
public class InvalidStateBean {

	public boolean invalidState(@XPath("/order/customer/shippingAddress/state/text()") String state) {
		boolean stateValid = isStateValid(state);
		return !stateValid;
	}

	private boolean isStateValid(String state) {

		String csvFile = "states/states.csv";
		String line = "";
		String cvsSplitBy = ",";
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			while ((line = br.readLine()) != null) {
				String[] lines = line.split(cvsSplitBy);
				if (lines[1].equals(state)) {
					return true;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
