package com.redhat.training.jb421.model;

import java.io.Serializable;

import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.annotation.Link;

@Link
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;
	@DataField(pos=5)
	private Integer id;
	@DataField(pos=6)
	private String firstName;
	@DataField(pos=7)
	private String lastName;
	@DataField(pos=8)
	private String username;
	@DataField(pos=9)
	private String password;
	@DataField(pos=10)
	private String email;
	@DataField(pos=11)
	private Boolean admin;
	@DataField(pos=12)
	private String streetAddress1;
	@DataField(pos=13)
	private String streetAddress2;
	@DataField(pos=14)
	private String streetAddress3;
	@DataField(pos=15)
	private String city;
	@DataField(pos=16)
	private String state;
	@DataField(pos=17)
	private String postalCode;
	@DataField(pos=18)
	private String country;
	
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
	
	


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public String getStreetAddress1() {
		return streetAddress1;
	}

	public void setStreetAddress1(String streetAddress1) {
		this.streetAddress1 = streetAddress1;
	}

	public String getStreetAddress2() {
		return streetAddress2;
	}

	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2;
	}

	public String getStreetAddress3() {
		return streetAddress3;
	}

	public void setStreetAddress3(String streetAddress3) {
		this.streetAddress3 = streetAddress3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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
	
	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", username=" + username
				+ ", password=" + password + ", email=" + email + ", admin=" + admin + ", streetAddress1="
				+ streetAddress1 + ", streetAddress2=" + streetAddress2 + ", streetAddress3=" + streetAddress3
				+ ", city=" + city + ", state=" + state + ", postalCode=" + postalCode + ", country=" + country + "]";
	}

}
