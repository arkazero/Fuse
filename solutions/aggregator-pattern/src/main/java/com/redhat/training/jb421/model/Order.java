package com.redhat.training.jb421.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.annotation.FixedLengthRecord;


@FixedLengthRecord(length=25)
@XmlRootElement
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	@DataField(pos = 1,length=6)
	private Integer id;
	@DataField(pos = 2, length=8, pattern="MM-dd-YY")
	private Date orderDate = new Date();
	@DataField(pos = 3, length=5, pattern="##.##")
	private BigDecimal discount;
	@DataField(pos = 5, length=6)
	private Integer customerId;
	
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

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((discount == null) ? 0 : discount.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
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
		if (orderDate == null) {
			if (other.orderDate != null)
				return false;
		} else if (!orderDate.equals(other.orderDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderDate=" + orderDate + ", discount=" + discount + ", customerId=" + customerId
				+ "]";
	}
	
	

}
