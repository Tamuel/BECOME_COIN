package gui.draw;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

import javax.swing.JPanel;

import dataObjects.CoinData;
import drawingObjects.DrawingObject;
import resource.CoinColor;

public class CanvasPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, FocusListener {

	private CoinData coinData;
	
	private ToolMode toolMode;
	
	private boolean isScalable;
	
	private int scaleOffset;
	private double scale;
	
	private int greed;
	private int pressedX;
	private int pressedY;
	private int draggedX;
	private int draggedY;
	private int releasedX;
	private int releasedY;
	
	private DrawingObject selectedObject;

	public CanvasPanel(CoinData coinData) {
		this.setBackground(CoinColor.WHITE);
		this.setLayout(null);
		
		this.addMouseWheelListener(this);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addFocusListener(this);
		
		isScalable = false;
		scaleOffset = 10;
		greed = 5;
		this.coinData = coinData;
	}

	public double getScaleOffset() {
		return this.scaleOffset;
	}
	
	public int getGreed() {
		return this.greed;
	}
	
	public void setGreed(int greed) {
		if(greed >= 5 && greed <= 30)
			this.greed = greed;
	}
	
	public Point getGreedPoint(int x, int y) {
		int mouseX = (int)getMousePosition().getX();
		int mouseY = (int)getMousePosition().getY();
		
		int newX;
		int newY;
		
		newX = (mouseX % greed) < (greed - (mouseX % greed)) ? (mouseX - (mouseX % greed)) : (mouseX + (greed - (mouseX % greed)));
		newY = (mouseY % greed) < (greed - (mouseY % greed)) ? (mouseY - (mouseY % greed)) : (mouseY + (greed - (mouseY % greed)));
		
		Point point = new Point();
		point.setLocation(newX, newY);
		
		return point;
	}
	
	public Area getLineCollisionArea(DrawingObject line) {
		double dist = Math.sqrt(
				(Math.pow(Math.abs(line.getBeginPoint().getX() - line.getEndPoint().getX()), 2)) +
				(Math.pow(Math.abs(line.getBeginPoint().getY() - line.getEndPoint().getY()), 2))) +
				10;
		Rectangle rect = new Rectangle((int)line.getBeginPoint().getX() - 5 - (line.getThickness() / 2),
				(int)line.getBeginPoint().getY() - 5 - (line.getThickness() / 2),
				(int)dist + line.getThickness(),
				10 + line.getThickness());
		double vecy = line.getEndPoint().getY() - line.getBeginPoint().getY();
		double vecx = line.getEndPoint().getX() - line.getBeginPoint().getX();
		double theta = Math.atan2(vecy, vecx);
		AffineTransform at = new AffineTransform();
		at.rotate(theta, line.getBeginPoint().getX(), line.getBeginPoint().getY());
		Area a = new Area(rect);
		a.transform(at);
		
		return a;
	}
	
	public boolean checkCollision(DrawingObject object) {
		switch(object.getToolMode()) {
		case LINE:
			if(getLineCollisionArea(object).contains(getMousePosition()))
				return true;
			break;
		case RECT:
			break;
		case CIRCLE:
			break;
		case ICON:
			break;
		case TAG:
			break;
		case BEACON:
			break;
		default:
			break;
		}
		return false;
	}
	
	// LINE
	public void pressedLine() {
		pressedX = (int)getMousePosition().getX();
		pressedY = (int)getMousePosition().getY();
		
		pressedX = (int)getGreedPoint(pressedX, pressedY).getX();
		pressedY = (int)getGreedPoint(pressedX, pressedY).getY();
		
		Point point = new Point();
		point.setLocation(pressedX, pressedY);
		
		coinData.getDrawingObject().setBeginPoint(point);
		coinData.getDrawingObject().setEndPoint(point);
	}
	
	public void draggedLine() {
		draggedX = (int)getMousePosition().getX();
		draggedY = (int)getMousePosition().getY();
		
		draggedX = (int)getGreedPoint(draggedX, draggedY).getX();
		draggedY = (int)getGreedPoint(draggedX, draggedY).getY();
		
		Point point = new Point();
		point.setLocation(draggedX, draggedY);
		
		coinData.getDrawingObject().setEndPoint(point);
	}
	
	public void releasedLine() {
		releasedX = (int)getMousePosition().getX();
		releasedY = (int)getMousePosition().getY();
		releasedX = (int)getGreedPoint(releasedX, releasedY).getX();
		releasedY = (int)getGreedPoint(releasedX, releasedY).getY();
	}
	
	// RECT
	public void pressedRect() {
		pressedX = (int)getMousePosition().getX();
		pressedY = (int)getMousePosition().getY();
		
		pressedX = (int)getGreedPoint(pressedX, pressedY).getX();
		pressedY = (int)getGreedPoint(pressedX, pressedY).getY();
		
		Point point = new Point();
		point.setLocation(pressedX, pressedY);
		
		coinData.getDrawingObject().setBeginPoint(point);
		coinData.getDrawingObject().setEndPoint(new Point(0, 0));
	}
	
	public void draggedRect() {
		draggedX = (int)getMousePosition().getX();
		draggedY = (int)getMousePosition().getY();
		
		draggedX = Math.abs((int)getGreedPoint(draggedX, draggedY).getX());
		draggedY = Math.abs((int)getGreedPoint(draggedX, draggedY).getY());
		
		Point point = new Point();
		point.setLocation(Math.min(pressedX, draggedX), Math.min(pressedY, draggedY));
		coinData.getDrawingObject().setBeginPoint(point);

		draggedX = Math.abs(draggedX - (int)pressedX);
		draggedY = Math.abs(draggedY - (int)pressedY);

		
		point = new Point();
		point.setLocation(draggedX, draggedY);
		coinData.getDrawingObject().setEndPoint(point);
	}
	
	public void releasedRect() {
		releasedLine();
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
		// TODO
		pressedX = (int)getMousePosition().getX();
		pressedY = (int)getMousePosition().getY();
		
		coinData.setSelectedObject(null);
		
		for(int i = 0; i < coinData.getDrawingObjectList().size(); i++) {
			DrawingObject object = coinData.getDrawingObjectList().get(i);
			switch(object.getToolMode()) {
			case LINE:
				if(checkCollision(object) == true) {
					coinData.setSelectedObject(object);
				}
				break;
			case RECT:
				break;
			case CIRCLE:
				break;
			case ICON:
				break;
			case TAG:
				break;
			case BEACON:
				break;
			default:
				
				break;
			}
		}
	}
	
	public void draggedSelect() {
		// TODO
	}
	
	public void releasedSelect() {
		// TODO
		this.repaint();
	}
	
	// ICON
	public void pressedIcon() {
		// TODO
	}
	
	public void draggedIcon() {
		// TODO
	}
	
	public void releasedIcon() {
		// TODO
	}
	
	// TAG
	public void pressedTag() {
		// TODO
	}
	
	public void draggedTag() {
		// TODO
	}
	
	public void releasedTag() {
		// TODO
	}
	
	
	// BEACON
	public void pressedBeacon() {
		// TODO
	}
	
	public void draggedBeacon() {
		// TODO
	}
	
	public void releasedBeacon() {
		// TODO
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
		}
		coinData.getDrawingObjectList().add(coinData.getDrawingObject());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
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
				newObject.setKey(tempObject.getKey());
			}
			if(pressedX == releasedX && pressedY == releasedY) {
				coinData.getDrawingObjectList().remove(coinData.getDrawingObjectList().size() - 1);
			}
			coinData.setDrawingObject(newObject);
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(coinData.getDrawingObject().getToolMode() != null) {
			switch(coinData.getDrawingObject().getToolMode()) {
			case SELECT:
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

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		if(isScalable == true) {
			if(arg0.getWheelRotation() < 0) {
				if(getScaleOffset() < 100) {
					if(getScaleOffset() >= 10) {
						this.scaleOffset += 10;
					}
					else {
						this.scaleOffset += 1;
					}
				}
			}
			else if(arg0.getWheelRotation() > 0) {
				if(getScaleOffset() > 1) {
					if(getScaleOffset() > 10) {
						this.scaleOffset -= 10;
					}
					else {
						this.scaleOffset -= 1;
					}
				}
			}
			scale = getScaleOffset();
			this.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ALT) {
			isScalable = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ALT) {
			isScalable = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		for(int i = 0; i < coinData.getDrawingObjectList().size(); i++) {
			DrawingObject object = coinData.getDrawingObjectList().get(i);
			g2.setStroke(new BasicStroke(object.getThickness()));
			switch(object.getToolMode()) {
			case SELECT:
				if(coinData.getSelectedObject() != null) {
					g2.setColor(CoinColor.ORANGE);
					switch(coinData.getSelectedObject().getToolMode()) {
					case LINE:
						this.drawRotatedRect(g2, coinData.getSelectedObject());
						break;
					}
				}
				break;
			case LINE:
				g2.setColor(object.getLineColor());
				g2.drawLine((int)object.getBeginPoint().getX(),
						(int)object.getBeginPoint().getY(),
						(int)object.getEndPoint().getX(),
						(int)object.getEndPoint().getY());
				break;
			case RECT:
				if(object.getFillColor() == null) {
					g2.setColor(object.getLineColor());
					g2.drawRect((int)object.getBeginPoint().getX(),
							(int)object.getBeginPoint().getY(),
							(int)object.getEndPoint().getX(),
							(int)object.getEndPoint().getY());
				}
				else {
					g2.setColor(object.getFillColor());
					g2.fillRect((int)object.getBeginPoint().getX(),
							(int)object.getBeginPoint().getY(),
							(int)object.getEndPoint().getX(),
							(int)object.getEndPoint().getY());
					g2.setColor(object.getLineColor());
					g2.drawRect((int)object.getBeginPoint().getX(),
							(int)object.getBeginPoint().getY(),
							(int)object.getEndPoint().getX(),
							(int)object.getEndPoint().getY());
				}
				break;
			case CIRCLE:
				if(object.getFillColor() == null) {
					g2.setColor(object.getLineColor());
					g2.drawOval((int)object.getBeginPoint().getX(),
							(int)object.getBeginPoint().getY(),
							(int)object.getEndPoint().getX(),
							(int)object.getEndPoint().getY());
				}
				else {
					g2.setColor(object.getFillColor());
					g2.fillOval((int)object.getBeginPoint().getX(),
							(int)object.getBeginPoint().getY(),
							(int)object.getEndPoint().getX(),
							(int)object.getEndPoint().getY());
					g2.setColor(object.getLineColor());
					g2.drawOval((int)object.getBeginPoint().getX(),
							(int)object.getBeginPoint().getY(),
							(int)object.getEndPoint().getX(),
							(int)object.getEndPoint().getY());
				}
				break;
			case ICON:
				break;
			case TAG:
				break;
			case BEACON:
				break;
			default:
				break;
			}
		}
	}
	
	public void drawRotatedRect(Graphics2D g2, DrawingObject line) {
		Area a = getLineCollisionArea(line);
		g2.setColor(CoinColor.ORANGE);
		g2.setStroke(new BasicStroke(1));
		g2.draw(a);
	}

	@Override
	public void focusGained(FocusEvent arg0) {
	}

	@Override
	public void focusLost(FocusEvent arg0) {
	}
}
