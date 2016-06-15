package com.coin.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "tbl_floor")
public class FloorPlan {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	//FloorName
	private String name;
	
	private String buildingName;
	private int floor;
	private String thumbNail;
	private String description;
	
	//Address
	private Double latitude;
	private Double longitude;
	
	private String ownerId;
	
	@ManyToOne (cascade = CascadeType.ALL)
	private CoinUser user;
	
	@OneToMany (mappedBy="floorPlan", cascade = CascadeType.ALL)
	private List<FloorObject> objects;
	
	public FloorPlan() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean addFloorObject(FloorObject temp) {
		if(objects == null)
			objects = new ArrayList<FloorObject>();
		
		return this.objects.add(temp);
	}
	
	public void removeObject(FloorObject fObject) {
		if(objects != null) {
			fObject.setFloorPlan(null);
			objects.remove(fObject);
		}	
	}
	
	public void removeAllPlan() {
		objects.forEach(plan->plan.setFloorPlan(null));
		this.objects.clear();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public String getThumbNail() {
		return thumbNail;
	}

	public void setThumbNail(String thumbNail) {
		this.thumbNail = thumbNail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public CoinUser getUser() {
		return user;
	}

	public void setUser(CoinUser user) {
		this.user = user;
	}

	public List<FloorObject> getObjects() {
		return objects;
	}

	public void setObjects(List<FloorObject> objects) {
		this.objects = objects;
	}

	@Override
	public String toString() {
		return String.format("%s:%s:%s:%s:%d:%f:%f:%s",
				id,name,buildingName,description,floor,
				latitude,longitude,thumbNail);
	}
}
