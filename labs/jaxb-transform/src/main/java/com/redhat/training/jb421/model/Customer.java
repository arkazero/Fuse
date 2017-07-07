package com.redhat.training.jb421.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String email;
	private Boolean admin;
	private Address billingAddress;
	private Address shippingAddress;
	private Set<Order> orders = new HashSet<Order>();

	public Customer() {

	}

	public Customer(String firstName, String lastName, String username,
			String password, String email) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.admin = false;
	}

	public Customer(String firstName, String lastName, String username,
			String password, String email, Boolean admin) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.admin = admin;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public Integer getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		Customer other = (Customer) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

}
