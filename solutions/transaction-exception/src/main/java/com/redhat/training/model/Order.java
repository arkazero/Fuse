package com.redhat.training.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "order_")
@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@XmlAttribute
	private int id;
	@XmlElement
	private String name;
	@XmlElement
	private String item;
	@XmlElement
	private Integer quantity;
	@XmlElement
	private BigDecimal extendedAmount;

	public Order() {

	}

	public Order(int id, String name, String item, int quantity, BigDecimal extendedAmount) {
		this.id = id;
		this.name = name;
		this.item = item;
		this.quantity = quantity;
		this.extendedAmount = extendedAmount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getExtendedAmount() {
		return extendedAmount;
	}

	public void setExtendedAmount(BigDecimal extendedAmount) {
		this.extendedAmount = extendedAmount;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", name=" + name + ", item=" + item + ", quantity=" + quantity + ", extendedAmount="
				+ extendedAmount + "]";
	}

}
