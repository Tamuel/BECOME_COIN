package dataObjects;

import java.util.ArrayList;
import java.util.Date;

import drawingObjects.DrawingObject;

public class CoinRoom {

	private String owner;
	private String name;
	private Date date;
	
	private ArrayList<DrawingObject> drawingObject = null;
	
	public CoinRoom() {
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

	public ArrayList<DrawingObject> getDrawingObject() {
		return drawingObject;
	}

	public void setDrawingObject(ArrayList<DrawingObject> drawingObject) {
		this.drawingObject = drawingObject;
	}
}
