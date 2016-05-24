package drawingObjects;

import java.awt.Color;
import java.awt.Point;

import gui.draw.ToolMode;

public class DrawingObject {

	private ToolMode toolMode;
	private Point beginPoint;
	private Point endPoint;
	private Color lineColor;
	private Color fillColor;
	private int thickness;
	private int key;
	
	public DrawingObject() {
		thickness = 1;
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

	public void setBeginPoint(Point beginPosition) {
		this.beginPoint = beginPosition;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPosition) {
		this.endPoint = endPosition;
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

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}
	
	
}