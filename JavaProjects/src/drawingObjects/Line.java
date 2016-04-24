package drawingObjects;

import java.awt.Color;
import java.awt.Point;

import resource.DimenR;

/**
 * Line Object
 * Have start position, end position, thickness, type and color
 * @author DongKyu
 *
 */

public class Line extends DrawingObject {
	
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
	
	public Line() { }
	
	public Line(Point startPoint) {
		setStartPosition(startPoint);
	}
	
	public Line(int startPosX, int startPosY) {
		setStartPosition(new Point(startPosX, startPosY));
	}
	
	public Line(Point startPos, Point endPos) {
		setStartPosition(startPos);
		setEndPosition(endPos);
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
	
	public void setLineType(LineType type) {
		this.type = type;
	}
	
	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public ObjectType getType() {
		return ObjectType.LINE;
	}

	@Override
	public VertexType getIntersectVertex(Point p) {
//		double lineDistance = getStartPosition().distance(getEndPosition());
//		double startToPDistance = getStartPosition().distance(p);
//		double endToPDistance = getEndPosition().distance(p);
//		double underLine = lineDistance * (endToPDistance / (endToPDistance + startToPDistance));
//		double distance = Math.sqrt(Math.pow(endToPDistance, 2) - Math.pow(underLine, 2));
		if(p == null)
			return null;
		
		if(getStartPosition().distance(p) <= DimenR.VERTEX_RADIUS)
			return VertexType.START;
		
		if(getEndPosition().distance(p) <= DimenR.VERTEX_RADIUS )
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
