package dataObjects;

import java.util.ArrayList;

import drawingObjects.DrawingObject;

public class CoinData {

	private ArrayList<DrawingObject> history;
	private ArrayList<DrawingObject> drawingObjectList;
	private DrawingObject drawingObject;
	private DrawingObject selectedObject;
	
	public CoinData() {
		drawingObjectList = new ArrayList<DrawingObject>();
		history = new ArrayList<DrawingObject>();
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

	public ArrayList<DrawingObject> getHistory() {
		return history;
	}

	public void setHistory(ArrayList<DrawingObject> history) {
		this.history = history;
	}

	public DrawingObject getSelectedObject() {
		return selectedObject;
	}

	public void setSelectedObject(DrawingObject selectedObject) {
		this.selectedObject = selectedObject;
	}
}
