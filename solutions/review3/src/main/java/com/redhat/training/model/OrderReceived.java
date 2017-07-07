package com.redhat.training.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderReceived implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String customerName;
	private String itemName;
	private int quantity;
	private BigDecimal price;

	public OrderReceived() {
		
	}
	
	public OrderReceived(int id, String customerName, String itemName, int quantity, BigDecimal price) {
		this.id = id;
		this.customerName = customerName;
		this.itemName = itemName;
		this.quantity = quantity;
		this.price = price;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		OrderReceived other = (OrderReceived) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderReceived [id=" + id + ", customerName=" + customerName + ", itemName=" + itemName + ", quantity="
				+ quantity + ", price=" + price + "]";
	}

}
