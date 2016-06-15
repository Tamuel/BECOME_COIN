package com.coin.domain;

import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "tbl_object")
public class FloorObject {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String toolMode;
	private int thickness;
	private String iconType;
	private int beginX;
	private int beginY;
	private int endX;
	private int endY;
	private int lineColor;
	private int fillColor;
	private String majorKey;
	private String minorKey;
	
	private boolean fill = false;
	private boolean line = false;
	private boolean isIcon = false;
	
	@ManyToOne (cascade = CascadeType.ALL)
	private FloorPlan floorPlan;
	
	public FloorObject() {
		toolMode = null;
		iconType = null;
		thickness = 1;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getToolMode() {
		return toolMode;
	}

	public void setToolMode(String toolMode) {
		this.toolMode = toolMode;
	}

	public int getThickness() {
		return thickness;
	}

	public void setThickness(int thickness) {
		this.thickness = thickness;
	}

	public String getIconType() {
		return iconType;
	}

	public void setIconType(String iconType) {
		this.iconType = iconType;
	}

	public int getBeginX() {
		return beginX;
	}

	public void setBeginX(int beginX) {
		this.beginX = beginX;
	}

	public int getBeginY() {
		return beginY;
	}

	public void setBeginY(int beginY) {
		this.beginY = beginY;
	}

	public int getEndX() {
		return endX;
	}

	public void setEndX(int endX) {
		this.endX = endX;
	}

	public int getEndY() {
		return endY;
	}

	public void setEndY(int endY) {
		this.endY = endY;
	}

	public int getLineColor() {
		return lineColor;
	}

	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}

	public int getFillColor() {
		return fillColor;
	}

	public void setFillColor(int fillColor) {
		this.fillColor = fillColor;
	}

	public String getMajorKey() {
		return majorKey;
	}

	public void setMajorKey(String majorKey) {
		this.majorKey = majorKey;
	}

	public String getMinorKey() {
		return minorKey;
	}

	public void setMinorKey(String minorKey) {
		this.minorKey = minorKey;
	}

	public boolean isFill() {
		return fill;
	}

	public void setFill(boolean fill) {
		this.fill = fill;
	}

	public boolean isLine() {
		return line;
	}

	public void setLine(boolean line) {
		this.line = line;
	}

	public boolean isIcon() {
		return isIcon;
	}

	public void setIcon(boolean isIcon) {
		this.isIcon = isIcon;
	}

	public FloorPlan getFloorPlan() {
		return floorPlan;
	}

	public void setFloorPlan(FloorPlan floorPlan) {
		this.floorPlan = floorPlan;
	}

	@Override
	public String toString() {
		return String.format("%s:%d:%d:%d:%d:%d:%d:%d",
				this.getToolMode(),this.getThickness(),
				this.getBeginX(),this.getBeginY(),
				this.getEndX(),this.getEndY(),
				this.getLineColor(),this.getFillColor());
	}
	
	public ArrayList<FloorObject> getObject(String objectData) {
		String stringTemp = objectData.replace("%0x0A", "\n");
		String line[] = stringTemp.split("\n");
		
		ArrayList<FloorObject> tempObjects = new ArrayList<>();
		
		for(String temp : line) {
			String data[] = temp.split(":");
			FloorObject newObject = new FloorObject();
			switch(data[0]) {
				case "RECT":
					if(newObject.getToolMode() == null)
						newObject.setToolMode("RECT");
				case "CIRCLE":
					if(newObject.getToolMode() == null)
						newObject.setToolMode("CIRCLE");
				case "LINE":
					if(newObject.getToolMode() == null)
						newObject.setToolMode("LINE");
					
				newObject.setThickness(Integer.parseInt(data[1]));
				newObject.setBeginX(Integer.parseInt(data[2]));
				newObject.setBeginY(Integer.parseInt(data[3]));
				if(newObject.getToolMode() != "LINE") {
					newObject.setEndX(Integer.parseInt(data[4]) + Integer.parseInt(data[2]));
					newObject.setEndY(Integer.parseInt(data[5]) + Integer.parseInt(data[3]));
				}
				else {
					newObject.setEndX(Integer.parseInt(data[4]));
					newObject.setEndY(Integer.parseInt(data[5]));
				}
				if(!data[6].equals("null")) {
					newObject.setLineColor(Integer.parseInt(data[6]));
					newObject.setLine(true);
				}
				if(!data[7].equals("null")) {
					newObject.setFillColor(Integer.parseInt(data[7]));
					newObject.setFill(true);
				}
				newObject.setIcon(false);
				break;
				
				case "TEXT":
					if(newObject.getToolMode() == null)
						newObject.setToolMode("TEXT");
				case "ICON":
					if(newObject.getToolMode() == null)
						newObject.setToolMode("ICON");
				case "BEACON":
					if(newObject.getToolMode() == null)
						newObject.setToolMode("BEACON");
				case "TAG":
					if(newObject.getToolMode() == null)
						newObject.setToolMode("TAG");
					
				newObject.setBeginX(Integer.parseInt(data[2])+25);
				newObject.setBeginY(Integer.parseInt(data[3])+25);
				newObject.setEndX(Integer.parseInt(data[2])+25);
				newObject.setEndY(Integer.parseInt(data[3])+25);
				if(newObject.getToolMode() != "ICON") {
					newObject.setMajorKey(data[6]);
					newObject.setMinorKey(data[7]);
				}
				//newObject.setIcon
				newObject.setLine(true);
				newObject.setIcon(true);
				break;
			}
			if(newObject.getToolMode() != null)
				tempObjects.add(newObject);
		}
		return tempObjects;
	}
}
