package com.redhat.training.jb421.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement(name = "catalogItem")
@XmlAccessorType(XmlAccessType.FIELD)
public class CatalogItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@XmlElement
	private Integer id;
	@XmlElement
	private String sku;
	@XmlElement
	private String title;
	@XmlElement
	private BigDecimal price;
	@XmlElement
	private String description;
	@XmlElement
	private String author;
	@XmlElement
	private String imagePath;
	@XmlElement
	private String category;
	@XmlElement
	private Boolean newItem;
	@ManyToOne
	@JoinColumn(name = "pub_id")
	@XmlTransient
	private Publisher publisher;
	
	public CatalogItem() {
		
	}

	public CatalogItem(String sku, String title, BigDecimal price,
			String description, String author, String imagePath,
			String category, Boolean newItem, Publisher publisher) {

		this.sku = sku;
		this.title = title;
		this.price = price;
		this.description = description;
		this.author = author;
		this.imagePath = imagePath;
		this.category = category;
		this.newItem = newItem;
		this.publisher = publisher;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Integer getId() {
		return id;
	}

	public Boolean getNewItem() {
		return newItem;
	}

	public void setNewItem(Boolean newItem) {
		this.newItem = newItem;
	}
	
	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		CatalogItem other = (CatalogItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
