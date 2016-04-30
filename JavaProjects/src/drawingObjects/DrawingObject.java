package drawingObjects;

import java.awt.Color;
import java.awt.Point;

import drawingObjects.ObjectType;
import drawingObjects.VertexType;

public abstract class DrawingObject {

	abstract public Point getStartPosition();
	abstract public void setStartPosition(Point p);
	abstract public Point getEndPosition();
	abstract public void setEndPosition(Point p);
	abstract public ObjectType getType();
	abstract public VertexType getIntersectVertex(Point p);
	abstract public Point getIntersectPosition(VertexType t);
	abstract public Color getColor();
	abstract public void setColor(Color color);
}
