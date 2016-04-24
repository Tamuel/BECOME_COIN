package drawingObjects;

import java.awt.Color;
import java.awt.Point;

import resource.DimenR;

/**
 * Rectangle Object
 * Have start position, end position, thickness, type and color
 * @author DongKyu
 *
 */

public class Rectangle extends DrawingObject {
	
	/** Point for start position */
	private Point startPosition;
	/** Point for end position */
	private Point endPosition;
	/** Integer for thickness */
	private int thickness;
	/** LineType for line type */
	private LineType type;
	/** Color for line color */
	private Color color;
	
	public Rectangle(Point startPoint) {
		setStartPosition(startPoint);
	}
	
	public Rectangle(int startX, int startY) {
		setStartPosition(new Point(startX, startY));
	}
	
	public Point getStartPosition() {
		return startPosition;
	}
	
	public void setStartPosition(Point startPosition) {
		this.startPosition = startPosition;
	}
	
	public Point getEndPosition() {
		return endPosition;
	}
	
	public void setEndPosition(Point endPosition) {
		this.endPosition = endPosition;
	}
	
	public int getThickness() {
		return thickness;
	}
	
	public void setThickness(int thickness) {
		this.thickness = thickness;
	}
	
	public LineType getLineType() {
		return type;
	}
	
	public void setType(LineType type) {
		this.type = type;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public ObjectType getType() {
		return ObjectType.RECTANGLE;
	}

	@Override
	public VertexType getIntersectVertex(Point p) {
		if(p == null)
			return null;
		
		if(getStartPosition().distance(p) <= DimenR.VERTEX_RADIUS)
			return VertexType.START;
		
		if(getEndPosition().distance(p) <= DimenR.VERTEX_RADIUS)
			return VertexType.END;

		return null;
	}
	
	@Override
	public Point getIntersectPosition(VertexType t) {
		switch(t) {
		case START:
			return getStartPosition();
		
		case END:
			return getEndPosition();
		
		default:
			return null;
		}
	}
}
