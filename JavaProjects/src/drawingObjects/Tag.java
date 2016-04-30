package drawingObjects;

import java.awt.Color;
import java.awt.Point;

import resource.DimenR;

/**
 * Tag Object
 * Have position, key and data
 * @author DongKyu
 *
 */

public class Tag extends DrawingObject {
	
	/** Point for position */
	private Point position;
	/** Long integer for key */
	private long key;
	/** String for extra data */
	private String data;
	
	private boolean isTaged = false;
	
	public Tag(int startPosX, int startPosY) {
		setPosition(new Point(startPosX, startPosY));
	}
	
	public Tag(Point startPoint) {
		setPosition(startPoint);
	}
	
	public Point getPosition() {
		return position;
	}
	
	public void setPosition(Point position) {
		this.position = position;
	}
	
	public long getKey() {
		return key;
	}
	
	public void setKey(long key) {
		this.key = key;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public Point getStartPosition() {
		return getPosition();
	}
	
	@Override
	public Point getEndPosition() {
		return getPosition();
	}
	
	@Override
	public ObjectType getType() {
		return ObjectType.TAG;
	}

	@Override
	public VertexType getIntersectVertex(Point p) {
		if(getPosition().distance(p) <= DimenR.VERTEX_RADIUS)
			return VertexType.START;
		
		return null;
	}

	@Override
	public void setStartPosition(Point p) {
		setPosition(p);
	}

	@Override
	public void setEndPosition(Point p) {
		setPosition(p);
	}
	
	@Override
	public Point getIntersectPosition(VertexType t) {
		switch(t) {
		case START:
		case END:
			return getPosition();
		
		default:
			return null;
		}
	}

	@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setColor(Color color) {
		// TODO Auto-generated method stub
		
	}

	public boolean isTaged() {
		return isTaged;
	}

	public void setTaged(boolean isTaged) {
		this.isTaged = isTaged;
	}
	
	
}
