package com.coin.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CoinObject {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String toolMode;
	private int thickness; //or IconMode
	private String iconType;
	private int beginX;
	private int beginY;
	private int endX;
	private int endY;
	private int lineColor;
	private int fillColor;
	private String majorKey;
	private String minorKey;
	
	public CoinObject() {
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
	
	@Override
	public String toString() {
		return String.format("%s:%s:%s:%s:%s:%s:%s:%s",
				toolMode,thickness,beginX,beginY,
				endX,endY,lineColor,fillColor);
	}
}
