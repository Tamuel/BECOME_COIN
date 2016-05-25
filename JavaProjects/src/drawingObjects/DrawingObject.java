package drawingObjects;

import java.awt.Color;
import java.awt.Point;

import gui.draw.ToolMode;
import resource.CoinIcon;

public class DrawingObject {

	private ToolMode toolMode;
	private Point beginPoint;
	private Point endPoint;
	private Color lineColor;
	private Color fillColor;
	private int thickness;
	private CoinIcon icon;
	private String majorKey;
	private String minorKey;
	private double theta;
	
	public DrawingObject() {
		thickness = 1;
		theta = 0;
	}

	public ToolMode getToolMode() {
		return toolMode;
	}

	public void setToolMode(ToolMode toolMode) {
		this.toolMode = toolMode;
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

	public CoinIcon getIcon() {
		return icon;
	}

	public void setIcon(CoinIcon icon) {
		this.icon = icon;
	}

	public double getTheta() {
		return theta;
	}

	public void setTheta(double theta) {
		this.theta = theta;
	}
}