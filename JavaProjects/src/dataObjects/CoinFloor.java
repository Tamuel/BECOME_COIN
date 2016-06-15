package dataObjects;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;

import drawingObjects.DrawingObject;

public class CoinFloor {

	private int id;
    private String name;
    private String buildingName;
    private String description;
    private int floor;
    private double longitude;
    private double latitude;
    private ImageIcon floorPlanImage;

	
	public CoinFloor() {
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


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public int getFloor() {
		return floor;
	}


	public void setFloor(int floor) {
		this.floor = floor;
	}


	public double getLongitude() {
		return longitude;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public ImageIcon getFloorPlanImage() {
		return floorPlanImage;
	}


	public void setFloorPlanImage(ImageIcon floorPlanImage) {
		this.floorPlanImage = floorPlanImage;
	}

	
}