package com.coin.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FloorData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String name;
	
	private String description;
	private String position;
	private String multiMediaData;
	
	public FloorData() {
		// TODO Auto-generated constructor stub
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getMultiMediaData() {
		return multiMediaData;
	}

	public void setMultiMediaData(String multiMediaData) {
		this.multiMediaData = multiMediaData;
	}
	
}
