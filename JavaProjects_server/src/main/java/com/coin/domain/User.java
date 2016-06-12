package com.coin.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {

	@Id
	private String id;
	
	private String password;
	private String name;
	
	@OneToMany( mappedBy = "user")
	private List<FloorPlan> floorPlans;

	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FloorPlan> getFloorPlans() {
		return floorPlans;
	}

	public void setFloorPlans(List<FloorPlan> floorPlans) {
		this.floorPlans = floorPlans;
	}
}
