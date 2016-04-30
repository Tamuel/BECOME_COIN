package drawingObjects;

import java.awt.Color;
import java.awt.Point;

import resource.DimenR;

/**
 * Oval Object
 * Have position, radius, thickness, type and color
 * @author DongKyu
 *
 */

public class Oval extends DrawingObject {
	
	/** Point for position */
	private Point startPosition;
	/** Integer for oval radius */
	private Point endPosition;
	/** Integer for thickness */
	private int thickness;
	/** LineType for line type */
	private LineType type;
	/** Color for line color */
	private Color color;
	
	public Oval(Point startPoint) {
		setStartPosition(startPoint);
	}
	
	public Oval(int xPos, int yPos) {
		setStartPosition(new Point(xPos, yPos));
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

	public void setStartPosition(Point position) {
		this.startPosition = position;
	}
	
	public void setEndPosition(Point endPosition) {
		this.endPosition = endPosition;
	}

	@Override
	public Point getStartPosition() {
		return startPosition;
	}
	
	@Override
	public Point getEndPosition() {
		return endPosition;
	}
	
	@Override
	public ObjectType getType() {
		return ObjectType.OVAL;
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
