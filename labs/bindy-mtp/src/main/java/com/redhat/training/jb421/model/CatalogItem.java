package com.redhat.training.jb421.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator=",",crlf="WINDOWS")
public class CatalogItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@DataField(pos=1)
	private Integer id;
	@DataField(pos=2)
	private String sku;
	@DataField(pos=3)
	private String title;
	@DataField(pos=4,decimalSeparator=".",precision=2)
	private BigDecimal price;
	@DataField(pos=5)
	private String description;
	@DataField(pos=6)
	private String author;
	@DataField(pos=7)
	private String imagePath;
	@DataField(pos=8)
	private String category;
	@DataField(pos=9)
	private Boolean newItem;
	
	public CatalogItem() {
		
	}

	public CatalogItem(String sku, String title, BigDecimal price,
			String description, String author, String imagePath,
			String category, Boolean newItem) {

		this.sku = sku;
		this.title = title;
		this.price = price;
		this.description = description;
		this.author = author;
		this.imagePath = imagePath;
		this.category = category;
		this.newItem = newItem;
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
	
	public void setId(Integer id){
		this.id = id;
	}

	public Boolean isNewItem() {
		return newItem;
	}

	public void setNewItem(Boolean newItem) {
		this.newItem = newItem;
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
