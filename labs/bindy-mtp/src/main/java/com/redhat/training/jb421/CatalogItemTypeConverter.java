package com.redhat.training.jb421;

import org.apache.camel.Converter;

import com.redhat.training.jb421.model.CatalogItem;

@Converter
public class CatalogItemTypeConverter {

	@Converter
	public String toNewItemMessage(CatalogItem item){
		if(item.isNewItem()){
			return "New Item! "+item.getTitle()+" is new to the store.";
		}else{
			return "Title:"+item.getTitle();
		}
	}

}
