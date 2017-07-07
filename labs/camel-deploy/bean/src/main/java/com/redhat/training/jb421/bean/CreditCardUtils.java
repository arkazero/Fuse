package com.redhat.training.jb421.bean;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

public class CreditCardUtils {

	public boolean validate(String ccNumber) throws Exception {
		return isCreditCardNumberValid(ccNumber);
	}

	boolean isCreditCardNumberValid(String creditCardNumber) {
		boolean isValid = false;
		String reversedNumber = new StringBuffer(creditCardNumber).reverse().toString();
		int mod10Count = 0;
		for (int i = 0; i < reversedNumber.length(); i++) {
			int augend = parseInt(valueOf(reversedNumber.charAt(i)));
			if (((i + 1) % 2) == 0) {
				String productString = valueOf(augend * 2);
				augend = 0;
				for (int j = 0; j < productString.length(); j++) {
					augend += parseInt(valueOf(productString.charAt(j)));
				}
			}
			mod10Count += augend;
		}
		if ((mod10Count % 10) == 0) {
			isValid = true;
		}
		return isValid;
	}

}
