package gui.draw;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import dataObjects.CoinData;
import drawingObjects.DrawingObject;
import gui.component.CoinButton;
import gui.component.CoinFrame;
import gui.select.SelectionFrame;
import resource.CoinColor;
import resource.CoinFont;

public class DrawingFrame extends CoinFrame implements ActionListener, MouseWheelListener, MouseListener, KeyListener{
	
	private CoinData coinData;
	
	private ExitCheckFrame exitCheckFrame;
	
	private CanvasPanel canvasPanel;
	private ToolPanel toolPanel;
	private ToolAttributePanel toolAttribPanel;
	
	private JLabel markLabel;
	private JLabel nameLabel;
	
	private JLabel scaleLabel;
	private JLabel greedLabel;
	private JLabel infoLabel;

	private CoinButton greedUpButton;
	private CoinButton greedDownButton;
	private CoinButton exitSaveButton;
	
	public DrawingFrame() {
		super("Coin Drawing Frame", 1280, 800);
		
		coinData = new CoinData();
		
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
		canvasPanel = new CanvasPanel(coinData);
		canvasPanel.setBorder(new LineBorder(CoinColor.DARK_GRAY, 2));
		canvasPanel.addMouseListener(this);
		canvasPanel.addKeyListener(this);
		this.add(canvasPanel).setBounds(50, 140, 950, 580);
		
		toolPanel = new ToolPanel(coinData);
		toolPanel.setBorder(new LineBorder(CoinColor.DARK_GRAY, 2));
		this.add(toolPanel).setBounds(1033, 140, 200, 300);
		
		toolAttribPanel = new ToolAttributePanel(coinData);
		toolAttribPanel.setBorder(new LineBorder(CoinColor.DARK_GRAY, 2));
		this.add(toolAttribPanel).setBounds(1033, 460, 200, 260);
		
		toolPanel.addListener(this);
		canvasPanel.addListener(this);
		toolAttribPanel.addListener(this);
	}
	
	public void addLabels() {
		scaleLabel = new JLabel("X " +  Double.toString(canvasPanel.getScaleOffset() / 10));
		scaleLabel.setFont(CoinFont.BIG_FONT_BOLD);
		this.add(scaleLabel).setBounds(60, 720, 200, 50);
		
		greedLabel = new JLabel("Greed " + canvasPanel.getGreed());
		greedLabel.setFont(CoinFont.BIG_FONT_BOLD);
		this.add(greedLabel).setBounds(110, 720, 300, 50);
		
		infoLabel = new JLabel("");
		infoLabel.setFont(CoinFont.BIG_FONT_BOLD);
		infoLabel.setForeground(CoinColor.ORANGE);
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(infoLabel).setBounds(400, 720, 300, 50);
	}
	
	public void addButtons() {
		exitSaveButton = new CoinButton("저장 / 종료");
		this.add(exitSaveButton).setBounds(900, 100, 100, 30);
		exitSaveButton.addActionListener(this);
		
		greedUpButton = new CoinButton("△");
		greedUpButton.setFont(CoinFont.BIG_FONT_BOLD);
		this.add(greedUpButton).setBounds(185, 735, 20, 20);
		greedUpButton.setVisible(false);
		greedUpButton.addActionListener(this);
		
		greedDownButton = new CoinButton("▽");
		greedDownButton.setFont(CoinFont.BIG_FONT_BOLD);
		this.add(greedDownButton).setBounds(210, 735, 20, 20);
		greedDownButton.setVisible(false);
		greedDownButton.addActionListener(this);
	}
	
	public void saveData() {
		// TODO save procedure
		for(int i = 0; i < coinData.getDrawingObjectList().size(); i++) {
			DrawingObject object = coinData.getDrawingObjectList().get(i);
			System.out.print(object.getToolMode() + ":");
			if(object.getToolMode() == ToolMode.ICON) {
				try {
					System.out.print(object.getIcon() + ":");
				} catch (Exception e) {
					System.out.print("null" + ":");
				}
			}
			else {
				System.out.print(object.getThickness() + ":");
			}
			System.out.print((int)object.getBeginPoint().getX() + ":");
			System.out.print((int)object.getBeginPoint().getY() + ":");
			System.out.print((int)object.getEndPoint().getX() + ":");
			System.out.print((int)object.getEndPoint().getY() + ":");
			if(object.getToolMode() == ToolMode.LINE ||
					object.getToolMode() == ToolMode.RECT ||
					object.getToolMode() == ToolMode.CIRCLE) {
				System.out.print(object.getLineColor().getRGB() + ":");
				try {
					System.out.print(object.getFillColor().getRGB());
				} catch (Exception e) {
					System.out.print("null");
				}
			}
			else if(object.getToolMode() == ToolMode.ICON) {
				System.out.print(object.getTheta() + ":");
				System.out.print("null");
			}
			else {
				System.out.print(object.getMajorKey() + ":");
				System.out.print(object.getMinorKey());
			}
			System.out.println("");
		}
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
			canvasPanel.repaint();
		}
		
		if(exitCheckFrame != null) {
			if(e.getSource() == exitCheckFrame.getExitWithSaveButton()) {
				saveData();
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
		greedLabel.setText("Greed " + canvasPanel.getGreed());
		this.repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		if(coinData.getDrawingObject().getToolMode() == ToolMode.SELECT) {
			toolAttribPanel.showAttributes();
			toolAttribPanel.repaint();
		}
		canvasPanel.repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(coinData.getDrawingObject().getToolMode() == ToolMode.SELECT) {
			toolAttribPanel.showAttributes();
			toolAttribPanel.repaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_CONTROL) {
			this.scaleLabel.setForeground(CoinColor.ORANGE);
			this.infoLabel.setText("Scale Offset Change");
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_SHIFT) {
			this.greedLabel.setForeground(CoinColor.ORANGE);
			this.infoLabel.setText("Greed Offset Change");
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_SPACE) {
			this.infoLabel.setText("Screen Move");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
			this.scaleLabel.setForeground(CoinColor.BLACK);
		}
		else if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
			this.greedLabel.setForeground(CoinColor.BLACK);
		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			
		}
		this.infoLabel.setText("");
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
