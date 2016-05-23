package gui.draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import resource.CoinColor;

public class CoinToolAttribPanel extends JPanel implements ActionListener{
	
	private static final Color[] colorList = {CoinColor.BLACK, CoinColor.THEME_COLOR, CoinColor.OBJECT_COLOR_1, CoinColor.OBJECT_COLOR_2};
	
	private static final int THICK_PADDING = 30;
	private static final int THICK_LABEL_X = 50;
	private static final int THICK_LABEL_Y = 30;
	private static final int THICK_LABEL_WIDTH = 130;
	private static final int THICK_LABEL_HEIGHT = 50;
	private static final int THICK_BUTTON_X = 20;
	private static final int THICK_BUTTON_Y = 20;
	private static final int THICK_BUTTON_SIZE = 20;
	private static final int COLOR_PADDING = 40;
	private static final int COLOR_BUTTON_SIZE = 30;
	private static final int LINE_COLOR_X = 25;
	private static final int LINE_COLOR_Y = 160;
	private static final int FILL_COLOR_X = 25;
	private static final int FILL_COLOR_Y = 220;
	
	private Color lineColor;
	private Color fillColor;
	private int thickness;
	
	private ArrayList<JButton> thickButtonList;
	private ArrayList<JButton> lineColorButtonList;
	private ArrayList<JButton> fillColorButtonList;
	private JButton selectedLineColorButton = null;
	private JButton selectedFillColorButton = null;
	private JButton selectedThickButton = null;
	
	private JLabel lineColorLabel;
	private JLabel fillColorLabel;
	
	public CoinToolAttribPanel() {
		this.setBackground(CoinColor.WHITE);
		this.setLayout(null);
		
		setup();
	}
	
	public void setup() {
		setupPrimitive();
		this.removeAll();
	}
	
	public void setupPrimitive() {
		thickButtonList = new ArrayList<JButton>();
		lineColorButtonList = new ArrayList<JButton>();
		fillColorButtonList = new ArrayList<JButton>();
		
		lineColorLabel = new JLabel("Line Color");
		lineColorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lineColorLabel.setBounds(50, 120, 100, 50);
		
		fillColorLabel = new JLabel("Fill Color");
		fillColorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fillColorLabel.setBounds(50, 180, 100, 50);
		
		for(int i = 0; i < 4; i++) {
			ThickLabel label = new ThickLabel(1 + (i * 2));
			this.add(label).setBounds(THICK_LABEL_X, THICK_LABEL_Y + (i * THICK_PADDING), THICK_LABEL_WIDTH, THICK_LABEL_HEIGHT);
			
			JButton button = new JButton();
			button.setBackground(null);
			this.add(button).setBounds(THICK_BUTTON_X, THICK_BUTTON_Y + (i * THICK_PADDING), THICK_BUTTON_SIZE, THICK_BUTTON_SIZE);
			button.addActionListener(this);
			thickButtonList.add(button);
			
			if(i == 0) {
				this.thickness = 1;
				selectedThickButton = button;
				button.setBackground(CoinColor.ORANGE);
			}
			
			button = new JButton();
			button.setBackground(colorList[i]);
			button.setBorder(null);
			this.add(button).setBounds(LINE_COLOR_X + (i * COLOR_PADDING), LINE_COLOR_Y, COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE);
			button.addActionListener(this);
			lineColorButtonList.add(button);
			
			if(i == 0) {
				this.lineColor = colorList[0];
				selectedLineColorButton = button;
				button.setBorder(new LineBorder(CoinColor.ORANGE, 3));
			}
			
			button = new JButton();
			button.setBackground(colorList[i]);
			button.setBorder(null);
			this.add(button).setBounds(FILL_COLOR_X + (i * COLOR_PADDING), FILL_COLOR_Y, COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE);
			button.addActionListener(this);
			fillColorButtonList.add(button);
			
			if(i == 0) {
				this.lineColor = colorList[0];
				selectedFillColorButton = button;
				button.setBorder(new LineBorder(CoinColor.ORANGE, 3));
			}
		}
	}
	
	public void showAttributes(CoinToolMode toolMode) {
		this.removeAll();
		switch(toolMode) {
		case SELECT:
			select();
			break;
		case LINE:
			primitives(toolMode);
			break;
		case RECT:
			primitives(toolMode);
			break;
		case CIRCLE:
			primitives(toolMode);
			break;
		case ICON:
			icon();
			break;
		case TAG:
			tag();
			break;
		case BEACON:
			beacon();
			break;
		default:
			break;
		}
		this.repaint();
	}
	
	private void primitives(CoinToolMode toolMode) {
		for(int i = 0; i < 4; i++) {
			ThickLabel label = new ThickLabel(1 + (i * 2));
			this.add(label).setBounds(THICK_LABEL_X, THICK_LABEL_Y + (i * THICK_PADDING), THICK_LABEL_WIDTH, THICK_LABEL_HEIGHT);
			this.add(thickButtonList.get(i)).setBounds(THICK_BUTTON_X, THICK_BUTTON_Y + (i * THICK_PADDING), THICK_BUTTON_SIZE, THICK_BUTTON_SIZE);
			this.add(lineColorButtonList.get(i)).setBounds(LINE_COLOR_X + (i * COLOR_PADDING), LINE_COLOR_Y, COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE);
			this.add(lineColorLabel);
			if(toolMode != CoinToolMode.LINE) {
				this.add(fillColorLabel);
				this.add(fillColorButtonList.get(i)).setBounds(FILL_COLOR_X + (i * COLOR_PADDING), FILL_COLOR_Y, COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE);
			}
		}
	}
	
	private void select() {
		
	}
	
	private void icon() {
		
	}
	
	private void tag() {
		
	}
	
	private void beacon() {
		
	}
	
	private class ThickLabel extends JLabel{
		int thickness;
		public ThickLabel(int thickness) {
			this.thickness = thickness;
		}
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;
			
			g2.setStroke(new BasicStroke(thickness));
			g2.drawLine(0, 0, 130, 0);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(int i = 0; i < 4; i++) {
			if(e.getSource() == thickButtonList.get(i)) {
				this.thickness = 1 + (i * 2);
				if(selectedThickButton != null)
					selectedThickButton.setBackground(null);
				selectedThickButton = thickButtonList.get(i);
				selectedThickButton.setBackground(CoinColor.ORANGE);
				break;
			}
			if(e.getSource() == lineColorButtonList.get(i)) {
				this.lineColor = colorList[i];
				if(selectedLineColorButton != null)
					selectedLineColorButton.setBorder(null);
				selectedLineColorButton = lineColorButtonList.get(i);
				selectedLineColorButton.setBorder(new LineBorder(CoinColor.ORANGE, 3));
				break;
			}
			if(e.getSource() == fillColorButtonList.get(i)) {
				this.fillColor = colorList[i];
				if(selectedFillColorButton != null)
					selectedFillColorButton.setBorder(null);
				selectedFillColorButton = fillColorButtonList.get(i);
				selectedFillColorButton.setBorder(new LineBorder(CoinColor.ORANGE, 3));
				break;
			}
		}
	}
}
