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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "order_")
@NamedQuery(name="getUndeliveredOrders", query="select o from Order o where o.delivered = false")
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Date orderDate = new Date();
	private BigDecimal discount;
	private Boolean delivered=false;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "cust_id")
	private Customer customer;
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="promo_id")
	private Set<Promotion> promoCode;
	
	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "order_id")
	@XmlElementWrapper(name="orderItems")
	@XmlElement(name="orderItem")
	@JsonIgnore
	private Set<OrderItem> orderItems = new HashSet<OrderItem>();
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name = "payment_id")
	private Payment payment;
	
	@JsonIgnore
	@Transient
	private Date dateFulfilled;
	
	@JsonIgnore
	@Transient
	private String fulfilledBy;

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

	public Date getDateFulfilled() {
		return dateFulfilled;
	}

	public void setDateFulfilled(Date dateFulfilled) {
		this.dateFulfilled = dateFulfilled;
	}

	public String getFulfilledBy() {
		return fulfilledBy;
	}

	public void setFulfilledBy(String fulfilledBy) {
		this.fulfilledBy = fulfilledBy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((dateFulfilled == null) ? 0 : dateFulfilled.hashCode());
		result = prime * result + ((delivered == null) ? 0 : delivered.hashCode());
		result = prime * result + ((discount == null) ? 0 : discount.hashCode());
		result = prime * result + ((fulfilledBy == null) ? 0 : fulfilledBy.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((orderDate == null) ? 0 : orderDate.hashCode());
		result = prime * result + ((orderItems == null) ? 0 : orderItems.hashCode());
		result = prime * result + ((payment == null) ? 0 : payment.hashCode());
		result = prime * result + ((promoCode == null) ? 0 : promoCode.hashCode());
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
		if (dateFulfilled == null) {
			if (other.dateFulfilled != null)
				return false;
		} else if (!dateFulfilled.equals(other.dateFulfilled))
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
		if (fulfilledBy == null) {
			if (other.fulfilledBy != null)
				return false;
		} else if (!fulfilledBy.equals(other.fulfilledBy))
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
		if (orderItems == null) {
			if (other.orderItems != null)
				return false;
		} else if (!orderItems.equals(other.orderItems))
			return false;
		if (payment == null) {
			if (other.payment != null)
				return false;
		} else if (!payment.equals(other.payment))
			return false;
		if (promoCode == null) {
			if (other.promoCode != null)
				return false;
		} else if (!promoCode.equals(other.promoCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderDate=" + orderDate + ", discount=" + discount + ", delivered=" + delivered
				+ ", customer=" + customer + ", promoCode=" + promoCode + ", orderItems=" + orderItems + ", payment="
				+ payment + ", dateFulfilled=" + dateFulfilled + ", fulfilledBy=" + fulfilledBy + "]";
	}

}
