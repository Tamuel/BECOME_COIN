package gui.draw;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import dataObjects.CoinData;
import drawingObjects.DrawingObject;
import resource.CoinColor;

public class CanvasPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, FocusListener {

	private static final int COLL_PADD = 12;
	
	private CoinData coinData;
	
	private ToolMode toolMode;
	
	private boolean scalable;
	private boolean greedControl;
	
	private int scaleOffset;
	private double scale;
	
	private boolean move;
	
	private int zoomState;
	
	private int greed;
	private int pressedX;
	private int pressedY;
	private int greedPressedX;
	private int greedPressedY;
	private int draggedX;
	private int draggedY;
	private int greedDraggedX;
	private int greedDraggedY;
	private int releasedX;
	private int releasedY;
	private int greedReleasedX;
	private int greedReleasedY;
	private int currentX;
	private int currentY;
	private int greedCurrentX;
	private int greedCurrentY;
	
	private int moveX;
	private int moveY;
	
	private SelectMode selectMode;
	
	private Point prevBeginPoint;
	private Point prevEndPoint;

	public CanvasPanel(CoinData coinData) {
		this.setBackground(CoinColor.WHITE);
		this.setLayout(null);
		
		this.addMouseWheelListener(this);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addFocusListener(this);
		
		scalable = true;
		greedControl = false;
		scaleOffset = 10;
		greed = 10;
		scale = 1;
		moveX = 0;
		moveY = 0;
		
		zoomState = 0; // 0 no zoom; 1 zoom in; 2 zoom out
		
		selectMode = SelectMode.NONE;
		
		this.coinData = coinData;
	}

	public double getScaleOffset() {
		return this.scaleOffset;
	}
	
	public int getGreed() {
		return this.greed;
	}
	
	public void setGreed(int greed) {
		if(greed >= 10 && greed <= 100)
			this.greed = greed;
	}
	
	public Point getCurrentMousePoint() {
		return new Point(currentX, currentY);
	}
	
	public Point getGreedPoint(int x, int y) {
		int mouseX = x;
		int mouseY = y;
		
		int newX;
		int newY;
		
		newX = (mouseX % greed) < (greed - (mouseX % greed)) ? (mouseX - (mouseX % greed)) : (mouseX + (greed - (mouseX % greed)));
		newY = (mouseY % greed) < (greed - (mouseY % greed)) ? (mouseY - (mouseY % greed)) : (mouseY + (greed - (mouseY % greed)));
		
		Point point = new Point();
		point.setLocation(newX, newY);
		
		return point;
	}
	
	public Point getObjectCenter(DrawingObject object) {
		Point centerPoint;
		
		if(object.getToolMode() == ToolMode.LINE) {
			double x1 = object.getBeginPoint().getX();
			double y1 = object.getBeginPoint().getY();
			double x2 = object.getEndPoint().getX();
			double y2 = object.getEndPoint().getY();
			
			centerPoint = new Point((int)Math.abs(x2 - x1), (int)Math.abs(y2 - y1));
		}
		else {
			double x = object.getBeginPoint().getX();
			double y = object.getBeginPoint().getY();
			double width = object.getEndPoint().getX();
			double height = object.getEndPoint().getY();
			
			centerPoint = new Point((int)(x + width/2), (int)(y + height/2));
		}
		
		return centerPoint;
	}
	
	public double distanceFromCenter(DrawingObject object, Point point) {
		double distance;
		
		Point centerPoint = getObjectCenter(object);
		
		distance = Math.sqrt(
				Math.pow(centerPoint.getX() - point.getX(), 2) +
				Math.pow(centerPoint.getY() - point.getY(), 2));
		
		return distance;
	}
	
	public Area getLineCollisionArea(DrawingObject line) {
		double dist = Math.sqrt(
				(Math.pow(Math.abs(line.getBeginPoint().getX() - line.getEndPoint().getX()), 2)) +
				(Math.pow(Math.abs(line.getBeginPoint().getY() - line.getEndPoint().getY()), 2))) +
				10;
		Rectangle rect = new Rectangle((int)line.getBeginPoint().getX() - 5 - (line.getThickness() / 2),
				(int)line.getBeginPoint().getY() - 10 - (line.getThickness() / 2),
				(int)dist + line.getThickness(),
				20 + line.getThickness());
		double vecy = line.getEndPoint().getY() - line.getBeginPoint().getY();
		double vecx = line.getEndPoint().getX() - line.getBeginPoint().getX();
		double theta = Math.atan2(vecy, vecx);
		AffineTransform at = new AffineTransform();
		at.rotate(theta, line.getBeginPoint().getX(), line.getBeginPoint().getY());
		Area a = new Area(rect);
		a.transform(at);
		
		return a;
	}
	
	public Area getLineBeginCollisionArea(DrawingObject line) {
		double x = line.getBeginPoint().getX();
		double y = line.getBeginPoint().getY();
		
		return new Area(new Ellipse2D.Double(x - COLL_PADD/2, y - COLL_PADD/2, COLL_PADD, COLL_PADD));
	}
	
	public Area getLineEndCollisionArea(DrawingObject line) {
		double x = line.getEndPoint().getX();
		double y = line.getEndPoint().getY();
		
		return new Area(new Ellipse2D.Double(x - COLL_PADD/2, y - COLL_PADD/2, COLL_PADD, COLL_PADD));
	}
	
	public Area getRectCollisionArea(DrawingObject rect) {
		double x = rect.getBeginPoint().getX() - 10;
		double y = rect.getBeginPoint().getY() - 10;
		double width = rect.getEndPoint().getX() + 20;
		double height = rect.getEndPoint().getY() + 20;
		
		Area a = new Area(new Rectangle((int)x, (int)y, (int)width, (int)height));
		
		return a;
	}
	
	public Area getUpperCollisionArea(DrawingObject object) {
		double x = object.getBeginPoint().getX() - 10;
		double y = object.getBeginPoint().getY();
		double width = object.getEndPoint().getX() + 20;
		double height = object.getEndPoint().getY() + 20;
		
		return new Area(new Ellipse2D.Double(x + width/2 - COLL_PADD/2, y - COLL_PADD/2, COLL_PADD, COLL_PADD));
	}
	
	public Area getLowerCollisionArea(DrawingObject object) {
		double x = object.getBeginPoint().getX() - 10;
		double y = object.getBeginPoint().getY() - 20;
		double width = object.getEndPoint().getX() + 20;
		double height = object.getEndPoint().getY() + 20;
		
		return new Area(new Ellipse2D.Double(x + width/2 - COLL_PADD/2, y + height - COLL_PADD/2, COLL_PADD, COLL_PADD));
	}
	
	public Area getLeftCollisionArea(DrawingObject object) {
		double x = object.getBeginPoint().getX();
		double y = object.getBeginPoint().getY() - 10;
		double width = object.getEndPoint().getX() + 20;
		double height = object.getEndPoint().getY() + 20;
		
		return new Area(new Ellipse2D.Double(x - COLL_PADD/2, y + height/2 - COLL_PADD/2, COLL_PADD, COLL_PADD));
	}
	
	public Area getRightCollisionArea(DrawingObject object) {
		double x = object.getBeginPoint().getX() - 20;
		double y = object.getBeginPoint().getY() - 10;
		double width = object.getEndPoint().getX() + 20;
		double height = object.getEndPoint().getY() + 20;
		
		return new Area(new Ellipse2D.Double(x + width - COLL_PADD/2, y + height/2 - COLL_PADD/2, COLL_PADD, COLL_PADD));
	}
	
	public Area getCircleCollisionArea(DrawingObject circle) {
		double x = circle.getBeginPoint().getX() - 10;
		double y = circle.getBeginPoint().getY() - 10;
		double width = circle.getEndPoint().getX() + 20;
		double height = circle.getEndPoint().getY() + 20;
		
		Area a = new Area(new Ellipse2D.Double(x, y, width, height));
		
		return a;
	}
	
	public boolean checkCollision(DrawingObject object) {
		switch(object.getToolMode()) {
		case LINE:
			if(getLineCollisionArea(object).contains(getMousePosition()))
				return true;
			break;
		case RECT: case ICON: case TAG: case BEACON:
			if(getRectCollisionArea(object).contains(getMousePosition()))
				return true;
			break;
		case CIRCLE:
			if(getCircleCollisionArea(object).contains(getMousePosition()))
				return true;
			break;
		default:
			break;
		}
		return false;
	}
	
	// LINE
	public void pressedLine() {
		//greedPressedX = (int)getGreedPoint(pressedX, pressedY).getX();
		//greedPressedY = (int)getGreedPoint(pressedX, pressedY).getY();
		
		Point point = new Point();
		point.setLocation(greedPressedX, greedPressedY);
		
		coinData.getDrawingObject().setBeginPoint(point);
		coinData.getDrawingObject().setEndPoint(point);
	}
	
	public void draggedLine() {
		greedDraggedX = (int)getGreedPoint(draggedX, draggedY).getX();
		greedDraggedY = (int)getGreedPoint(draggedX, draggedY).getY();
		
		Point point = new Point();
		point.setLocation(greedDraggedX, greedDraggedY);
		
		coinData.getDrawingObject().setEndPoint(point);
	}
	
	public void releasedLine() {
		greedReleasedX = (int)getGreedPoint(releasedX, releasedY).getX();
		greedReleasedY = (int)getGreedPoint(releasedX, releasedY).getY();
		if(greedPressedX == greedReleasedX && greedPressedY == greedReleasedY) {
			coinData.getDrawingObjectList().remove(coinData.getDrawingObjectList().size() - 1);
		}
	}
	
	// RECT
	public void pressedRect() {
		//greedPressedX = (int)getGreedPoint(pressedX, pressedY).getX();
		//greedPressedY = (int)getGreedPoint(pressedX, pressedY).getY();
		
		Point point = new Point();
		point.setLocation(greedPressedX, greedPressedY);
		
		coinData.getDrawingObject().setBeginPoint(point);
		coinData.getDrawingObject().setEndPoint(new Point(0, 0));
	}
	
	public void draggedRect() {
		greedDraggedX = Math.abs((int)getGreedPoint(draggedX, draggedY).getX());
		greedDraggedY = Math.abs((int)getGreedPoint(draggedX, draggedY).getY());
		
		Point point = new Point();
		point.setLocation(Math.min(greedPressedX, greedDraggedX), Math.min(greedPressedY, greedDraggedY));
		coinData.getDrawingObject().setBeginPoint(point);

		greedDraggedX = Math.abs(greedDraggedX - (int)greedPressedX);
		greedDraggedY = Math.abs(greedDraggedY - (int)greedPressedY);
		
		point = new Point();
		point.setLocation(greedDraggedX, greedDraggedY);
		coinData.getDrawingObject().setEndPoint(point);
	}
	
	public void releasedRect() {
		releasedLine();
		if((int)coinData.getDrawingObject().getEndPoint().getX() == 0 ||
			(int)coinData.getDrawingObject().getEndPoint().getY() == 0) {
			coinData.getDrawingObjectList().remove(coinData.getDrawingObject());
		}
	}
	
	// CIRCLE
	public void pressedCircle() {
		pressedRect();
	}
	
	public void draggedCircle() {
		draggedRect();
	}
	
	public void releasedCircle() {
		releasedRect();
	}
	
	// SELECT
	public void pressedSelect() {
		//greedPressedX = (int)getGreedPoint(pressedX, pressedY).getX();
		//greedPressedY = (int)getGreedPoint(pressedX, pressedY).getY();
		
		DrawingObject object = null;
		DrawingObject temp = coinData.getSelectedObject();
		coinData.setSelectedObject(null);
		
		for(int i = 0; i < coinData.getDrawingObjectList().size(); i++) {
			object = coinData.getDrawingObjectList().get(i);
			if(checkCollision(object) == true) {
				prevBeginPoint = object.getBeginPoint().getLocation();
				prevEndPoint = object.getEndPoint().getLocation();
				selectMode = SelectMode.MOVE;
				if(object.getToolMode() == ToolMode.LINE && object == temp) {
					coinData.setSelectedObject(temp);
					break;
				}
				coinData.setSelectedObject(object);
			}
		}
		
		object = coinData.getSelectedObject();
		if(object != null) {
			switch(object.getToolMode()) {
			case LINE:
				if(getLineBeginCollisionArea(object).contains(getMousePosition())) {
					selectMode = SelectMode.LINE_BEGIN;
				}
				else if(getLineEndCollisionArea(object).contains(getMousePosition())) {
					selectMode = SelectMode.LINE_END;
				}
				break;
			case RECT: case CIRCLE:
				if(getUpperCollisionArea(object).contains(getMousePosition())) {
					selectMode = SelectMode.UPPER_SIDE;
				}
				else if(getLowerCollisionArea(object).contains(getMousePosition())) {
					selectMode = SelectMode.LOWER_SIDE;
				}
				else if(getLeftCollisionArea(object).contains(getMousePosition())) {
					selectMode = SelectMode.LEFT_SIDE;
				}
				else if(getRightCollisionArea(object).contains(getMousePosition())) {
					selectMode = SelectMode.RIGHT_SIDE;
				}
				break;
			case ICON:
				if(getUpperCollisionArea(object).contains(getMousePosition())) {
					selectMode = SelectMode.UPPER_SIDE;
				}
				break;
			case TAG: case BEACON:
				break;
			default:
				break;
			}
		}
	}
	
	public void draggedSelect() {
		greedDraggedX = (int)getGreedPoint(draggedX, draggedY).getX();
		greedDraggedY = (int)getGreedPoint(draggedX, draggedY).getY();
		
		int diffX = greedPressedX - greedDraggedX;
		int diffY = greedPressedY - greedDraggedY;
		
		DrawingObject object = coinData.getSelectedObject();
		Point beginPoint;
		Point endPoint;
		
		switch(selectMode) {
		case MOVE:
			beginPoint = new Point(
					(int)prevBeginPoint.getX() - diffX,
					(int)prevBeginPoint.getY() - diffY);
			if(object.getToolMode() == ToolMode.LINE) {
				endPoint = new Point(
					(int)prevEndPoint.getX() - diffX,
					(int)prevEndPoint.getY() - diffY);
			}
			else {
				endPoint = object.getEndPoint();
			}
			coinData.getSelectedObject().setBeginPoint(beginPoint);
			coinData.getSelectedObject().setEndPoint(endPoint);
			break;
		case LINE_BEGIN:
			beginPoint = new Point(
					(int)prevBeginPoint.getX() - diffX,
					(int)prevBeginPoint.getY() - diffY);
			coinData.getSelectedObject().setBeginPoint(beginPoint);
			break;
		case LINE_END:
			endPoint = new Point(
					(int)prevEndPoint.getX() - diffX,
					(int)prevEndPoint.getY() - diffY);
			coinData.getSelectedObject().setEndPoint(endPoint);
			break;
		case UPPER_SIDE:
			if(object.getToolMode() == ToolMode.RECT || object.getToolMode() == ToolMode.CIRCLE) {
				if((int)prevEndPoint.getY() + diffY < 0) {
					this.selectMode = SelectMode.LOWER_SIDE;
					prevBeginPoint.setLocation(prevBeginPoint.getX(),
							coinData.getSelectedObject().getBeginPoint().getY() +
							coinData.getSelectedObject().getEndPoint().getY());
					prevEndPoint.setLocation(prevEndPoint.getX(), 0);
					greedPressedX = greedDraggedX;
					greedPressedY = greedDraggedY;
					break;
				}
				beginPoint = new Point(
						(int)prevBeginPoint.getX(),
						(int)prevBeginPoint.getY() - diffY);
				endPoint = new Point(
						(int)prevEndPoint.getX(),
						(int)prevEndPoint.getY() + diffY);
				coinData.getSelectedObject().setBeginPoint(beginPoint);
				coinData.getSelectedObject().setEndPoint(endPoint);
			}
			else {
				double theta = Math.atan2(
						diffY,
						diffX);
				object.setTheta(theta);
			}
			break;
		case LOWER_SIDE:
			if((int)prevEndPoint.getY() - diffY < 0) {
				this.selectMode = SelectMode.UPPER_SIDE;
				prevBeginPoint.setLocation(prevBeginPoint.getX(),
						coinData.getSelectedObject().getBeginPoint().getY());
				prevEndPoint.setLocation(prevEndPoint.getX(), 0);
				greedPressedX = greedDraggedX;
				greedPressedY = greedDraggedY;
				break;
			}
			endPoint = new Point(
					(int)prevEndPoint.getX(),
					(int)prevEndPoint.getY() - diffY);
			coinData.getSelectedObject().setEndPoint(endPoint);
			break;
		case LEFT_SIDE:
			if((int)prevEndPoint.getX() + diffX < 0) {
				this.selectMode = SelectMode.RIGHT_SIDE;
				prevBeginPoint.setLocation(coinData.getSelectedObject().getBeginPoint().getX(),
						prevBeginPoint.getY());
				prevEndPoint.setLocation(0, prevEndPoint.getY());
				greedPressedX = greedDraggedX;
				greedPressedY = greedDraggedY;
				break;
			}
			beginPoint = new Point(
					(int)prevBeginPoint.getX() - diffX,
					(int)prevBeginPoint.getY());
			endPoint = new Point(
					(int)prevEndPoint.getX() + diffX,
					(int)prevEndPoint.getY());
			coinData.getSelectedObject().setBeginPoint(beginPoint);
			coinData.getSelectedObject().setEndPoint(endPoint);
			break;
		case RIGHT_SIDE:
			if((int)prevEndPoint.getX() - diffX < 0) {
				this.selectMode = SelectMode.LEFT_SIDE;
				prevBeginPoint.setLocation(prevBeginPoint.getX(),
						coinData.getSelectedObject().getBeginPoint().getY());
				prevEndPoint.setLocation(0, prevEndPoint.getY());
				greedPressedX = greedDraggedX;
				greedPressedY = greedDraggedY;
				break;
			}
			endPoint = new Point(
					(int)prevEndPoint.getX() - diffX,
					(int)prevEndPoint.getY());
			coinData.getSelectedObject().setEndPoint(endPoint);
			break;
		default:
			break;
		}
	}
	
	public void releasedSelect() {
		if(coinData.getSelectedObject() != null) {
			if(coinData.getSelectedObject().getToolMode() == ToolMode.LINE) {
				if((int)coinData.getSelectedObject().getBeginPoint().getX() == (int)coinData.getSelectedObject().getEndPoint().getX() &&
						(int)coinData.getSelectedObject().getBeginPoint().getY() == (int)coinData.getSelectedObject().getEndPoint().getY()) {
					coinData.getDrawingObjectList().remove(coinData.getSelectedObject());
					coinData.setSelectedObject(null);
				}
			}
			else {
				if((int)coinData.getSelectedObject().getEndPoint().getX() == 0 ||
						(int)coinData.getSelectedObject().getEndPoint().getY() == 0) {
					coinData.getDrawingObjectList().remove(coinData.getSelectedObject());
					coinData.setSelectedObject(null);
				}
			}
		}
		selectMode = SelectMode.NONE;
		this.repaint();
	}
	
	// ICON
	public void pressedIcon() {
		//greedPressedX = (int)getGreedPoint(pressedX, pressedY).getX();
		//greedPressedY = (int)getGreedPoint(pressedX, pressedY).getY();
		
		Point point = new Point();
		point.setLocation(greedPressedX - 25, greedPressedY - 25);
		
		coinData.getDrawingObject().setBeginPoint(point);
		coinData.getDrawingObject().setEndPoint(new Point(50, 50));
	}
	
	public void draggedIcon() {
		// TODO
	}
	
	public void releasedIcon() {
		if(coinData.getDrawingObject().getIcon() == null)
			coinData.getDrawingObjectList().remove(coinData.getDrawingObject());
	}
	
	// TAG
	public void pressedTag() {
		// TODO
		pressedIcon();
	}
	
	public void draggedTag() {
		// TODO
	}
	
	public void releasedTag() {
		// TODO
		releasedIcon();
	}
	
	
	// BEACON
	public void pressedBeacon() {
		// TODO
		pressedIcon();
	}
	
	public void draggedBeacon() {
		// TODO
	}
	
	public void releasedBeacon() {
		// TODO
		releasedIcon();
	}
	
	
	public void addListener(MouseWheelListener listener) {
		this.addMouseWheelListener(listener);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.grabFocus();
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		pressedX = (int)getMousePosition().getX();
		pressedY = (int)getMousePosition().getY();
		
		greedPressedX = (int)getGreedPoint(pressedX, pressedY).getX();
		greedPressedY = (int)getGreedPoint(pressedX, pressedY).getY();
		
		if(e.getButton() == 1) {
			move = false;
			
			if(coinData.getDrawingObject().getToolMode() != null) {
				switch(coinData.getDrawingObject().getToolMode()) {
				case SELECT:
					pressedSelect();
					break;
				case LINE:
					pressedLine();
					break;
				case RECT:
					pressedRect();
					break;
				case CIRCLE:
					pressedCircle();
					break;
				case ICON:
					pressedIcon();
					break;
				case TAG:
					pressedTag();
					break;
				case BEACON:
					pressedBeacon();
					break;
				default:
					break;
				}
				if(coinData.getDrawingObject().getToolMode() != ToolMode.SELECT)
					coinData.getDrawingObjectList().add(coinData.getDrawingObject());	
			}
		}
		else if(e.getButton() == 3) {
			move = true;
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		double tempDraggedX;
		double tempDraggedY;
		try {
			tempDraggedX = (int)getMousePosition().getX();
			tempDraggedY = (int)getMousePosition().getY();
			
			draggedX = (int)tempDraggedX;
			draggedY = (int)tempDraggedY;
		} catch(Exception exception) {}
		
		greedDraggedX = (int)getGreedPoint(draggedX, draggedY).getX();
		greedDraggedY = (int)getGreedPoint(draggedX, draggedY).getY();
		
		if(move == false) {
			if(coinData.getDrawingObject().getToolMode() != null) {
				switch(coinData.getDrawingObject().getToolMode()) {
				case SELECT:
					draggedSelect();
					break;
				case LINE:
					draggedLine();
					break;
				case RECT:
					draggedRect();
					break;
				case CIRCLE:
					draggedCircle();
					break;
				case ICON:
					draggedIcon();
					break;
				case TAG:
					draggedTag();
					break;
				case BEACON:
					draggedBeacon();
					break;
				default:
					break;
				}
				this.repaint();
			}
		}
		else if(move == true) {
			if(Math.abs((double)greedDraggedX - (double)greedPressedX) > 0 ||
					Math.abs((double)greedDraggedY - (double)greedPressedY) > 0) {
				for(int i = 0; i < coinData.getDrawingObjectList().size(); i++) {
					Point movedPoint = new Point(coinData.getDrawingObjectList().get(i).getBeginPoint());
					movedPoint.setLocation(
							movedPoint.getX() + greedDraggedX - greedPressedX,
							movedPoint.getY() + greedDraggedY - greedPressedY);
					coinData.getDrawingObjectList().get(i).setBeginPoint(movedPoint);
					if(coinData.getDrawingObjectList().get(i).getToolMode() == ToolMode.LINE) {
						movedPoint = new Point(coinData.getDrawingObjectList().get(i).getEndPoint());
						movedPoint.setLocation(
								movedPoint.getX() + greedDraggedX - greedPressedX,
								movedPoint.getY() + greedDraggedY - greedPressedY);
						coinData.getDrawingObjectList().get(i).setEndPoint(movedPoint);
					}
					this.repaint();
				}
				greedPressedX = greedDraggedX;
				greedPressedY = greedDraggedY;
				
				//moveX += greedDraggedX - greedPressedX;
				//moveY += greedDraggedY - greedPressedY;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		try {
			releasedX = (int)getMousePosition().getX();
			releasedY = (int)getMousePosition().getY();
		} catch(Exception exception) {
			releasedX = draggedX;
			releasedY = draggedY;
		}
		
		if(move == false) {			
			if(coinData.getDrawingObject().getToolMode() != null) {
				switch(coinData.getDrawingObject().getToolMode()) {
				case SELECT:
					releasedSelect();
					break;
				case LINE:
					releasedLine();
					break;
				case RECT:
					releasedRect();
					break;
				case CIRCLE:
					releasedCircle();
					break;
				case ICON:
					releasedIcon();
					break;
				case TAG:
					releasedTag();
					break;
				case BEACON:
					releasedBeacon();
					break;
				default:
					break;
				}
				DrawingObject tempObject = coinData.getDrawingObject();
				DrawingObject newObject = new DrawingObject();
				if(tempObject != null) {
					newObject.setToolMode(tempObject.getToolMode());
					newObject.setLineColor(tempObject.getLineColor());
					newObject.setFillColor(tempObject.getFillColor());
					newObject.setThickness(tempObject.getThickness());
					newObject.setMajorKey(tempObject.getMajorKey());
					newObject.setMinorKey(tempObject.getMinorKey());
					newObject.setIcon(tempObject.getIcon());
				}
				coinData.setDrawingObject(newObject);
			}
		}
		else if(move == true) {
			
		}
		
		repaint();
	}
	
	private void zoomIn() {
		for(int i = 0; i < coinData.getDrawingObjectList().size(); i++) {
			DrawingObject object = coinData.getDrawingObjectList().get(i);
			Point point;
			if(object.getToolMode() == ToolMode.LINE) {
				point = new Point();
				point.setLocation(object.getBeginPoint().getX() + (object.getBeginPoint().getX() - currentX),
						object.getBeginPoint().getY() + (object.getBeginPoint().getY() - currentY));
				object.setBeginPoint(point);
				point = new Point();
				point.setLocation(object.getEndPoint().getX() + (object.getEndPoint().getX() - currentX),
						object.getEndPoint().getY() + (object.getEndPoint().getY() - currentY));
				object.setEndPoint(point);
			}
			else {
				point = new Point();
				point.setLocation(object.getBeginPoint().getX() + (object.getBeginPoint().getX() - currentX),
						object.getBeginPoint().getY() + (object.getBeginPoint().getY() - currentY));
				object.setBeginPoint(point);
				if(object.getToolMode() == ToolMode.RECT || object.getToolMode() == ToolMode.CIRCLE) {
					point = new Point();
					point.setLocation(object.getEndPoint().getX() * 2,
							object.getEndPoint().getY() * 2);
					object.setEndPoint(point);
				}
			}
		}
	}
	
	private void zoomOut() {
		for(int i = 0; i < coinData.getDrawingObjectList().size(); i++) {
			DrawingObject object = coinData.getDrawingObjectList().get(i);
			Point point;
			if(object.getToolMode() == ToolMode.LINE) {
				point = new Point();
				point.setLocation(object.getBeginPoint().getX() - (object.getBeginPoint().getX() - currentX) / 2,
						object.getBeginPoint().getY() - (object.getBeginPoint().getY() - currentY) / 2);
				object.setBeginPoint(point);
				point = new Point();
				point.setLocation(object.getEndPoint().getX() - (object.getEndPoint().getX() - currentX) / 2,
						object.getEndPoint().getY() - (object.getEndPoint().getY() - currentY) / 2);
				object.setEndPoint(point);
			}
			else {
				point = new Point();
				point.setLocation(object.getBeginPoint().getX() - (object.getBeginPoint().getX() - currentX) / 2,
						object.getBeginPoint().getY() - (object.getBeginPoint().getY() - currentY) / 2);
				object.setBeginPoint(point);
				if(object.getToolMode() == ToolMode.RECT || object.getToolMode() == ToolMode.CIRCLE) {
					point = new Point();
					point.setLocation(object.getEndPoint().getX() / 2,
							object.getEndPoint().getY() / 2);
					object.setEndPoint(point);
				}
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		if(scalable == true) {
			if(arg0.getWheelRotation() < 0) {
				if(getScaleOffset() < 100) {
					scaleOffset *= 2;
					scale = getScaleOffset()/10;
					zoomIn();
					zoomState = 1;
				}
			}
			else if(arg0.getWheelRotation() > 0) {
				if(getScaleOffset() > 5) {
					scaleOffset /= 2;
					scale = getScaleOffset()/10;
					zoomOut();
					zoomState = 2;
				}
			}
			
			this.repaint();
		}
		if(greedControl == true) {
			if(arg0.getWheelRotation() < 0) {
				setGreed(getGreed() + 10);
			}
			else if(arg0.getWheelRotation() > 0) {
				setGreed(getGreed() - 10);
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		currentX = (int)this.getMousePosition().getX();
		currentY = (int)this.getMousePosition().getY();
		
		//System.out.println(coinData.getDrawingObjectList().size());
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		scalable = false;
		if(arg0.getKeyCode() == KeyEvent.VK_SHIFT) {
			greedControl = true;
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_DELETE) {
			if(coinData.getDrawingObject().getToolMode() == ToolMode.SELECT) {
				coinData.getDrawingObjectList().remove(coinData.getSelectedObject());
				coinData.setSelectedObject(null);
			}
			repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_CONTROL) {
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_SHIFT) {
			greedControl = false;
		}
		scalable = true;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		double zoomFactor = 1;
		
		switch(zoomState) {
		case 0:
			zoomFactor = 1;
			break;
		case 1:
			zoomFactor = 2;
			break;
		case 2:
			zoomFactor = 0.5;
			break;
		default:
			break;
		}
		
		for(int i = 0; i < coinData.getDrawingObjectList().size(); i++) {
			DrawingObject object = coinData.getDrawingObjectList().get(i);
			g2.setStroke(new BasicStroke(object.getThickness()));
			switch(object.getToolMode()) {
			case LINE:
				g2.setColor(object.getLineColor());
				g2.drawLine((int)object.getBeginPoint().getX(),
						(int)object.getBeginPoint().getY(),
						(int)object.getEndPoint().getX(),
						(int)object.getEndPoint().getY());
				break;
			case RECT:
				if(object.getFillColor() != null) {
					g2.setColor(object.getFillColor());
					g2.fillRect((int)object.getBeginPoint().getX(),
							(int)object.getBeginPoint().getY(),
							(int)(object.getEndPoint().getX()),
							(int)(object.getEndPoint().getY()));
				}
				g2.setColor(object.getLineColor());
				g2.drawRect((int)object.getBeginPoint().getX(),
						(int)object.getBeginPoint().getY(),
						(int)(object.getEndPoint().getX()),
						(int)(object.getEndPoint().getY()));
				break;
			case CIRCLE:
				if(object.getFillColor() != null) {
					g2.setColor(object.getFillColor());
					g2.fillOval((int)object.getBeginPoint().getX(),
							(int)object.getBeginPoint().getY(),
							(int)(object.getEndPoint().getX()),
							(int)(object.getEndPoint().getY()));
				}
				g2.setColor(object.getLineColor());
				g2.drawOval((int)object.getBeginPoint().getX(),
						(int)object.getBeginPoint().getY(),
						(int)(object.getEndPoint().getX()),
						(int)(object.getEndPoint().getY()));
				break;
			case ICON: case TAG: case BEACON:
				double locationX = object.getEndPoint().getX() / 2;
				double locationY = object.getEndPoint().getY() / 2;
				AffineTransform tx = AffineTransform.getRotateInstance(object.getTheta(), locationX, locationY);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
				BufferedImage bImage = new BufferedImage(
						(int)(object.getEndPoint().getX()),
						(int)(object.getEndPoint().getY()),
						BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2b = bImage.createGraphics();
				g2b.drawImage(object.getIcon().getImageIcon().getImage(), 0, 0, null);
				g2b.dispose();
				g2.drawImage(op.filter(bImage, null),
						(int)object.getBeginPoint().getX(),
						(int)object.getBeginPoint().getY(),
						null);
				break;
			default:
				break;
			}
		}
		
		if(coinData.getDrawingObject().getToolMode() == ToolMode.SELECT) {
			if(coinData.getSelectedObject() != null) {
				DrawingObject object = coinData.getSelectedObject();
				g2.setColor(CoinColor.ORANGE);
				g2.setStroke(new BasicStroke(2));
				switch(coinData.getSelectedObject().getToolMode()) {
				case LINE:
					//drawRotatedRect(g2, object);
					g2.draw(getLineBeginCollisionArea(object));
					g2.draw(getLineEndCollisionArea(object));
					break;
				case RECT: case CIRCLE:
					//g2.draw(getRectCollisionArea(object));
					g2.draw(getUpperCollisionArea(object));
					g2.draw(getLowerCollisionArea(object));
					g2.draw(getLeftCollisionArea(object));
					g2.draw(getRightCollisionArea(object));
					break;
				case ICON: case TAG: case BEACON:
					g2.draw(getUpperCollisionArea(object));
					break;
				default:
					break;
				}
			}
		}
		
		zoomState = 0;
	}
	
	public void drawRotatedRect(Graphics2D g2, DrawingObject line) {
		Area a = getLineCollisionArea(line);
		g2.setColor(CoinColor.ORANGE);
		g2.draw(a);
	}

	@Override
	public void focusGained(FocusEvent arg0) {
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		//coinData.setSelectedObject(null);
		//repaint();
	}
}
