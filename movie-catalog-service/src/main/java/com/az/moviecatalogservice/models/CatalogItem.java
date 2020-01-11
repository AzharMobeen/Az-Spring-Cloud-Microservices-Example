package com.az.moviecatalogservice.models;

import lombok.Data;


@Data
public class CatalogItem {

	public CatalogItem(String name, String description, int rating) {
		this.name = name;
		this.description = description;
		this.rating = rating;
	}
	private String name;
	private String description;
	private int rating;
	
}
