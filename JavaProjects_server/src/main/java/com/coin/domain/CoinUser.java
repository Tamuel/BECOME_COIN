package com.coin.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "tbl_user")
public class CoinUser {

	@Id
	private String id;
	
	private String password;
	private String name;
	
	@OneToMany (mappedBy="user", cascade = CascadeType.ALL)
	private List<FloorPlan> plans;
	
	public CoinUser() {
	}
	
	public boolean addFloorPlan(FloorPlan temp) {
		if(plans == null)
			plans = new ArrayList<FloorPlan>();
		
		return this.plans.add(temp);
	}
	
	public void removePlan(FloorPlan plan) {
		if(plans != null) {
			plan.setUser(null);
			plans.remove(plan);
		}
		else {
			plans.clear();
		}
	}
	
	public void removeAllPlan() {
		plans.forEach(plan->plan.setUser(null));
		this.plans.clear();
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

	public List<FloorPlan> getPlans() {
		return plans;
	}

	public void setPlans(List<FloorPlan> plans) {
		this.plans = plans;
	}
	
}
