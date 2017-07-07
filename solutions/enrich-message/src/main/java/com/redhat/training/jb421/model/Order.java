package com.redhat.training.jb421.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "order_")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Date orderDate = new Date();
	private BigDecimal discount;
	private Boolean delivered=false;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "cust_id")
	private Customer customer;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="promo_id")
	private Set<Promotion> promoCode;
	
	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "order_id")
	@XmlElementWrapper(name="orderItems")
	@XmlElement(name="orderItem")
	private Set<OrderItem> orderItems = new HashSet<OrderItem>();
	
	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "payment_id")
	private Payment payment;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<Promotion> getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(Set<Promotion> promoCode) {
		this.promoCode = promoCode;
	}

	public Integer getId() {
		return id;
	}

	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
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

}
