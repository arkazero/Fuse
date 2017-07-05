package com.redhat.training.jb421.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.annotation.Link;

@CsvRecord(separator="`")
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@DataField(pos=1)
	private Integer id;
	@DataField(pos=2, pattern="MM-dd-yyyy")
	private Date orderDate = new Date();
	@DataField(pos=3, pattern="##.##")
	private BigDecimal discount;
	@DataField(pos=4)
	private Boolean delivered=false;
	@Link
	private Customer customer;
	@DataField(pos=19)
	private String itemSku;
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public Boolean getDelivered() {
		return delivered;
	}

	public void setDelivered(Boolean delivered) {
		this.delivered = delivered;
	}

	public void deliver() {
		setDelivered(true);
	}
	
	public String getItemSku() {
		return itemSku;
	}

	public void setItemSku(String itemSku) {
		this.itemSku = itemSku;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((delivered == null) ? 0 : delivered.hashCode());
		result = prime * result + ((discount == null) ? 0 : discount.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((itemSku == null) ? 0 : itemSku.hashCode());
		result = prime * result + ((orderDate == null) ? 0 : orderDate.hashCode());
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
		Order other = (Order) obj;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (delivered == null) {
			if (other.delivered != null)
				return false;
		} else if (!delivered.equals(other.delivered))
			return false;
		if (discount == null) {
			if (other.discount != null)
				return false;
		} else if (!discount.equals(other.discount))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (itemSku == null) {
			if (other.itemSku != null)
				return false;
		} else if (!itemSku.equals(other.itemSku))
			return false;
		if (orderDate == null) {
			if (other.orderDate != null)
				return false;
		} else if (!orderDate.equals(other.orderDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderDate=" + orderDate + ", discount=" + discount + ", delivered=" + delivered
				+ ", customer=" + customer + ", itemSku=" + itemSku + "]";
	}

}
