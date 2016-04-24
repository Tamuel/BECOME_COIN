package dataObjects;

import java.util.ArrayList;
import java.util.Date;

import drawingObjects.DrawingObject;

public class CoinFloor {

	private String owner;
	private String name;
	private Date date;
	private String briefInfo;
	
	private ArrayList<CoinRoom> roomList = null;
	
	private ArrayList<DrawingObject> drawingObject = null;
	
	public CoinFloor() {
		setDate(new Date());
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getBriefInfo() {
		return briefInfo;
	}

	public void setBriefInfo(String briefInfo) {
		this.briefInfo = briefInfo;
	}

	public ArrayList<CoinRoom> getRoomList() {
		return roomList;
	}

	public void setRoomList(ArrayList<CoinRoom> roomList) {
		this.roomList = roomList;
	}

	public ArrayList<DrawingObject> getDrawingObject() {
		return drawingObject;
	}

	public void setDrawingObject(ArrayList<DrawingObject> drawingObject) {
		this.drawingObject = drawingObject;
	}
}