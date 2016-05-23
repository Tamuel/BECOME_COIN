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

import drawingObjects.DrawingObject;
import resource.CoinColor;

public class CanvasPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, FocusListener {

	private ArrayList<DrawingObject> drawingObjectList;
	private DrawingObject drawingObject;
	
	private ToolMode toolMode;
	
	private boolean isScalable;
	
	private int scaleOffset;
	private double scale;
	
	private boolean pressed;
	
	private boolean ready;

	public CanvasPanel(DrawingObject drawingObject) {
		this.setBackground(CoinColor.WHITE);
		this.setLayout(null);
		
		this.addMouseWheelListener(this);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addFocusListener(this);
		
		isScalable = false;
		scaleOffset = 10;
		
		drawingObjectList = new ArrayList<DrawingObject>();
		this.drawingObject = drawingObject;
		
		pressed = false;
	}

	public double getScaleOffset() {
		return scaleOffset;
	}
	
	public void setDrawingObject(DrawingObject drawingObject) {
		this.drawingObject = drawingObject;
	}
	
	public void pressedLine() {
		Point point = new Point();
		point.setLocation(getMousePosition().getX(), getMousePosition().getY());
		drawingObject.setBeginPosition(point);
		drawingObject.setEndPosition(point);
	}
	
	public void releasedLine() {
		
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
		while(!ready);
		if(drawingObject.getToolMode() != null) {
			switch(drawingObject.getToolMode()) {
			case SELECT:
				break;
			case LINE:
				pressedLine();
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
		drawingObjectList.add(drawingObject);
		pressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(drawingObject.getToolMode() != null) {
			switch(drawingObject.getToolMode()) {
			case SELECT:
				break;
			case LINE:
				releasedLine();
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
		pressed = false;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		Point point = new Point();
		point.setLocation(getMousePosition().getX(), getMousePosition().getY());
		drawingObject.setEndPosition(point);
		
		this.repaint();
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
		
		for(int i = 0; i < drawingObjectList.size(); i++) {
			//System.out.println(i + ": " +(int)drawingObjectList.get(i).getBeginPosition().getX() + " " +
			//		(int)drawingObjectList.get(i).getBeginPosition().getY());
			g2.setStroke(new BasicStroke(drawingObjectList.get(i).getThickness()));
			g2.setColor(drawingObjectList.get(i).getLineColor());
			g2.drawLine((int)drawingObjectList.get(i).getBeginPosition().getX(),
					(int)drawingObjectList.get(i).getBeginPosition().getY(),
					(int)drawingObjectList.get(i).getEndPosition().getX(),
					(int)drawingObjectList.get(i).getEndPosition().getY());
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {
	}

	@Override
	public void focusLost(FocusEvent arg0) {
	}
	
	public boolean isPressed() {
		return this.pressed;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}
}
