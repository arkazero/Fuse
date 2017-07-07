package com.redhat.training.jb421.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement
//@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class OrderItem implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Integer quantity;
	private BigDecimal extPrice;


	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getExtPrice() {
		return extPrice;
	}

	public void setExtPrice(BigDecimal extPrice) {
		this.extPrice = extPrice;
	}

	public Integer getId() {
		return id;
	}


	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", quantity=" + quantity + ", extPrice=" + extPrice + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((extPrice == null) ? 0 : extPrice.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
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
		OrderItem other = (OrderItem) obj;
		if (extPrice == null) {
			if (other.extPrice != null)
				return false;
		} else if (!extPrice.equals(other.extPrice))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		return true;
	}



}
