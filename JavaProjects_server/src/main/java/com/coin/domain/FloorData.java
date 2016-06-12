package com.coin.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class FloorData {

	@Id
	private String name;
	
	private String description;
	private String position;
	private String multiMediaData;
	
	@OneToOne
	private FloorPlan floorPlan;

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

	public FloorPlan getFloorPlan() {
		return floorPlan;
	}

	public void setFloorPlan(FloorPlan floorPlan) {
		this.floorPlan = floorPlan;
	}
	
	
}
