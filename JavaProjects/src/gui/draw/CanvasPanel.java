package gui.draw;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.sun.xml.internal.ws.dump.LoggingDumpTube.Position;

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
		greed = 10;
		this.coinData = coinData;
	}

	public double getScaleOffset() {
		return this.scaleOffset;
	}
	
	public int getGreed() {
		return this.greed;
	}
	
	public void pressedLine() {
		pressedX = (int)getMousePosition().getX();
		pressedY = (int)getMousePosition().getY();
		
		pressedX = pressedX - (pressedX % greed);
		pressedY = pressedY - (pressedY % greed);
		
		Point point = new Point();
		point.setLocation(pressedX, pressedY);
		
		coinData.getDrawingObject().setBeginPosition(point);
		coinData.getDrawingObject().setEndPosition(point);
	}
	
	public void draggedLine() {
		draggedX = (int)getMousePosition().getX();
		draggedY = (int)getMousePosition().getY();
		
		draggedX = draggedX - (draggedX % greed);
		draggedY = draggedY - (draggedY % greed);
		
		Point point = new Point();
		point.setLocation(draggedX, draggedY);
		
		coinData.getDrawingObject().setEndPosition(point);
	}
	
	public void releasedLine() {
		
	}
	
	public void pressedRect() {
		pressedX = (int)getMousePosition().getX();
		pressedY = (int)getMousePosition().getY();
		
		pressedX = pressedX - (pressedX % greed);
		pressedY = pressedY - (pressedY % greed);
		
		Point point = new Point();
		point.setLocation(pressedX, pressedY);
		
		coinData.getDrawingObject().setBeginPosition(point);
		coinData.getDrawingObject().setEndPosition(new Point(0, 0));
	}
	
	public void draggedRect() {
		boolean reverse = false;
		
		draggedX = (int)getMousePosition().getX();
		draggedY = (int)getMousePosition().getY();
		draggedX = Math.abs(draggedX - (draggedX % greed));
		draggedY = Math.abs(draggedY - (draggedY % greed));
		
		if(pressedX > draggedX || pressedY > draggedY) {
			reverse = true;
		}
		
		Point point = new Point();
		point.setLocation(Math.min(pressedX, draggedX), Math.min(pressedY, draggedY));
		coinData.getDrawingObject().setBeginPosition(point);

		draggedX = Math.abs(draggedX - (int)pressedX);
		draggedY = Math.abs(draggedY - (int)pressedY);

		
		point = new Point();
		point.setLocation(draggedX, draggedY);
		coinData.getDrawingObject().setEndPosition(point);
	}
	
	public void releasedRect() {
		
	}
	
	public void pressedCircle() {
		
	}
	
	public void draggedCircle() {
		
	}
	
	public void releasedCircle() {
		
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
		/*
		System.out.println(coinData.getDrawingObject().getToolMode());
		System.out.println(coinData.getDrawingObject().getThickness());
		System.out.println(coinData.getDrawingObject().getLineColor());
		*/
		if(coinData.getDrawingObject().getToolMode() != null) {
			switch(coinData.getDrawingObject().getToolMode()) {
			case SELECT:
				break;
			case LINE:
				pressedLine();
				break;
			case RECT:
				pressedRect();
				break;
			case CIRCLE:
				pressedRect();
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
		coinData.getDrawingObjectList().add(coinData.getDrawingObject());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(coinData.getDrawingObject().getToolMode() != null) {
			switch(coinData.getDrawingObject().getToolMode()) {
			case SELECT:
				break;
			case LINE:
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
		DrawingObject tempObject = coinData.getDrawingObject();
		DrawingObject newObject = new DrawingObject();
		newObject.setToolMode(tempObject.getToolMode());
		newObject.setLineColor(tempObject.getLineColor());
		newObject.setFillColor(tempObject.getFillColor());
		newObject.setThickness(tempObject.getThickness());
		newObject.setKey(tempObject.getKey());
		coinData.setDrawingObject(newObject);
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
				draggedRect();
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
		
		repaint();
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
		
		for(int i = 0; i < coinData.getDrawingObjectList().size(); i++) {
			DrawingObject object = coinData.getDrawingObjectList().get(i);
			g2.setStroke(new BasicStroke(object.getThickness()));
			g2.setColor(object.getLineColor());
			switch(object.getToolMode()) {
			case SELECT:
				break;
			case LINE:
				g2.drawLine((int)object.getBeginPosition().getX(),
						(int)object.getBeginPosition().getY(),
						(int)object.getEndPosition().getX(),
						(int)object.getEndPosition().getY());
				break;
			case RECT:
				if(object.getFillColor() == null) {
					g2.drawRect((int)object.getBeginPosition().getX(),
							(int)object.getBeginPosition().getY(),
							(int)object.getEndPosition().getX(),
							(int)object.getEndPosition().getY());
				}
				else {
					g2.setBackground(object.getFillColor());
					g2.fillRect((int)object.getBeginPosition().getX(),
							(int)object.getBeginPosition().getY(),
							(int)object.getEndPosition().getX(),
							(int)object.getEndPosition().getY());
				}
				break;
			case CIRCLE:
				if(object.getFillColor() == null) {
					g2.drawOval((int)object.getBeginPosition().getX(),
							(int)object.getBeginPosition().getY(),
							(int)object.getEndPosition().getX(),
							(int)object.getEndPosition().getY());
				}
				else {
					g2.setBackground(object.getFillColor());
					g2.fillOval((int)object.getBeginPosition().getX(),
							(int)object.getBeginPosition().getY(),
							(int)object.getEndPosition().getX(),
							(int)object.getEndPosition().getY());
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

	@Override
	public void focusGained(FocusEvent arg0) {
	}

	@Override
	public void focusLost(FocusEvent arg0) {
	}
}
