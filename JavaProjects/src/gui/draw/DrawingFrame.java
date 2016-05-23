package gui.draw;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import drawingObjects.DrawingObject;
import gui.component.CoinButton;
import gui.component.CoinFrame;
import gui.select.SelectionFrame;
import resource.CoinColor;
import resource.CoinFont;

public class DrawingFrame extends CoinFrame implements ActionListener, MouseListener, MouseWheelListener{
	
	private DrawingObject drawingObject;
	
	private ExitCheckFrame exitCheckFrame;
	
	private CanvasPanel canvasPanel;
	private ToolPanel toolPanel;
	private ToolAttributePanel toolAttribPanel;
	
	private JLabel markLabel;
	private JLabel nameLabel;
	
	private JLabel scaleLabel;

	private CoinButton exitSaveButton;
	
	public DrawingFrame() {
		super("Coin Drawing Frame", 1280, 800);
		
		drawingObject = new DrawingObject();
		
		addComponents();
	}
	
	public void addComponents() {
		addLogo();
		addPanels();
		addLabels();
		addButtons();
	}
	
	public void addLogo() {
		markLabel = new JLabel();
		markLabel.setIcon(new ImageIcon("resources/images/Mark_Transparent_100x100.png"));
		this.add(markLabel).setBounds(15, 10, 100, 100);
		
		nameLabel = new JLabel("Coin Editor");
		nameLabel.setFont(CoinFont.VERY_BIG_FONT);
		this.add(nameLabel).setBounds(110, 35, 200, 50);
		
		this.exitButton.addActionListener(this);
		this.minimizeButton.addActionListener(this);
	}
	
	public void addPanels() {
		canvasPanel = new CanvasPanel(drawingObject);
		canvasPanel.setBorder(new LineBorder(CoinColor.DARK_GRAY, 2));
		this.add(canvasPanel).setBounds(50, 140, 950, 580);
		
		toolPanel = new ToolPanel(drawingObject);
		toolPanel.setBorder(new LineBorder(CoinColor.DARK_GRAY, 2));
		this.add(toolPanel).setBounds(1033, 140, 200, 300);
		
		toolAttribPanel = new ToolAttributePanel(drawingObject);
		toolAttribPanel.setBorder(new LineBorder(CoinColor.DARK_GRAY, 2));
		this.add(toolAttribPanel).setBounds(1033, 460, 200, 260);
		
		toolPanel.addListener(this);
		canvasPanel.addListener(this);
		toolAttribPanel.addListener(this);
	}
	
	public void addLabels() {
		scaleLabel = new JLabel("X " +  Double.toString(canvasPanel.getScaleOffset() / 10));
		scaleLabel.setFont(CoinFont.BIG_FONT);
		this.add(scaleLabel).setBounds(60, 720, 200, 50);
	}
	
	public void addButtons() {
		exitSaveButton = new CoinButton("저장 / 종료");
		this.add(exitSaveButton).setBounds(900, 100, 100, 30);
		exitSaveButton.addActionListener(this);
	}
	
	public void exitProcedure() {
		SelectionFrame selectionFrame = new SelectionFrame();
		if(exitCheckFrame != null)
			exitCheckFrame.dispose();
		dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == exitButton) {
			exitProcedure();
		}
		else if(e.getSource() == minimizeButton) {
			setState(Frame.ICONIFIED);
		}
		else if(e.getSource() == exitSaveButton) {
			if(exitCheckFrame == null) {
				exitCheckFrame = new ExitCheckFrame();
				exitCheckFrame.addListener(this);
			}
			else
				exitCheckFrame.setVisible(true);
		}
		else {
			toolAttribPanel.showAttributes();
			try {
				this.wait();
			} catch(Exception ex) {}
			/*System.out.println("mode: " + drawingObject.getToolMode());
			System.out.println("thickness: " + drawingObject.getThickness());
			System.out.println("line color: " + drawingObject.getLineColor());
			System.out.println("fill color: " + drawingObject.getFillColor());
			System.out.println("");*/
		}
		
		if(exitCheckFrame != null) {
			if(e.getSource() == exitCheckFrame.getExitWithSaveButton()) {
				// TODO save procedure
				exitProcedure();
			}
			else if(e.getSource() == exitCheckFrame.getExitWithoutSaveButton()) {
				exitProcedure();
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		scaleLabel.setText("X " +  Double.toString(canvasPanel.getScaleOffset() / 10));
		this.repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		canvasPanel.setReady(false);
		DrawingObject temp = drawingObject; 
		drawingObject = new DrawingObject();
		drawingObject.setFillColor(temp.getFillColor());
		drawingObject.setLineColor(temp.getLineColor());
		drawingObject.setThickness(temp.getThickness());
		drawingObject.setToolMode(temp.getToolMode());
		canvasPanel.setDrawingObject(drawingObject);
		toolPanel.setDrawingObject(drawingObject);
		toolAttribPanel.setDrawingObject(drawingObject);
		canvasPanel.setReady(true);
	}
}
