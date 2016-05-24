package dataObjects;

import java.util.ArrayList;

import drawingObjects.DrawingObject;

public class CoinData {

	private ArrayList<DrawingObject> drawingObjectList;
	private DrawingObject drawingObject;
	
	public CoinData() {
		drawingObjectList = new ArrayList<DrawingObject>();
		drawingObject = new DrawingObject();
	}

	public ArrayList<DrawingObject> getDrawingObjectList() {
		return drawingObjectList;
	}

	public void setDrawingObjectList(ArrayList<DrawingObject> drawingObjectList) {
		this.drawingObjectList = drawingObjectList;
	}

	public DrawingObject getDrawingObject() {
		return drawingObject;
	}

	public void setDrawingObject(DrawingObject drawingObject) {
		this.drawingObject = drawingObject;
	}
}
