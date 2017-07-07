package com.redhat.training.jb421.model;

import java.io.Serializable;

public class Payment implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String number;
	private String expireMonth;
	private String expireYear;
	private String holderName;
	private PaymentType paymentType;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getExpireMonth() {
		return expireMonth;
	}

	public void setExpireMonth(String expireMonth) {
		this.expireMonth = expireMonth;
	}

	public String getExpireYear() {
		return expireYear;
	}

	public void setExpireYear(String expireYear) {
		this.expireYear = expireYear;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expireMonth == null) ? 0 : expireMonth.hashCode());
		result = prime * result + ((expireYear == null) ? 0 : expireYear.hashCode());
		result = prime * result + ((holderName == null) ? 0 : holderName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((paymentType == null) ? 0 : paymentType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payment other = (Payment) obj;
		if (expireMonth == null) {
			if (other.expireMonth != null)
				return false;
		} else if (!expireMonth.equals(other.expireMonth))
			return false;
		if (expireYear == null) {
			if (other.expireYear != null)
				return false;
		} else if (!expireYear.equals(other.expireYear))
			return false;
		if (holderName == null) {
			if (other.holderName != null)
				return false;
		} else if (!holderName.equals(other.holderName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (paymentType != other.paymentType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Payment [id=" + id + ", number=" + number + ", expireMonth=" + expireMonth + ", expireYear="
				+ expireYear + ", holderName=" + holderName + ", paymentType=" + paymentType + "]";
	}
	
	
	

}
