package com.coin.domain;

import java.awt.Color;
import java.awt.Point;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Object {

	@Id
	private int toolMode;
	
	private boolean fill;
	private boolean line;
	private Point beginPoint;
	private Point endPoint;
	private Color lineColor;
	private Color fillColor;
	private int thickness;
	private String icon;
	private String majorKey;
	private String minorKey;
	private double theta;
	
	@ManyToOne
	private FloorPlan floorPlan;

	public Object() {
		majorKey = "";
		minorKey = "";
		thickness = 1;
		theta = 0;
	}

	public int getToolMode() {
		return toolMode;
	}

	public void setToolMode(int toolMode) {
		this.toolMode = toolMode;
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

	public Point getBeginPoint() {
		return beginPoint;
	}

	public void setBeginPoint(Point beginPoint) {
		this.beginPoint = beginPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	public Color getLineColor() {
		return lineColor;
	}

	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public int getThickness() {
		return thickness;
	}

	public void setThickness(int thickness) {
		this.thickness = thickness;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public double getTheta() {
		return theta;
	}

	public void setTheta(double theta) {
		this.theta = theta;
	}
	
	public FloorPlan getFloorPlan() {
		return floorPlan;
	}

	public void setFloorPlan(FloorPlan floorPlan) {
		this.floorPlan = floorPlan;
	}
}
