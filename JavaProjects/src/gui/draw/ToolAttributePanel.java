package gui.draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import dataObjects.CoinData;
import drawingObjects.DrawingObject;
import gui.component.CoinImageButton;
import resource.CoinColor;
import resource.CoinIcon;

public class ToolAttributePanel extends JPanel implements ActionListener{
	
	private static final Color[] COLOR_LIST = {CoinColor.BLACK, CoinColor.THEME_COLOR, CoinColor.OBJECT_COLOR_1, CoinColor.OBJECT_COLOR_2};
	private static final int[] ICON_POSITION = {10, 75, 140, 205};
	private static final ImageIcon[] ICON_LIST = {
			CoinIcon.COIN_TOILET.getImageIcon(),
			CoinIcon.COIN_TAG.getImageIcon(),
			CoinIcon.CON_BEACON.getImageIcon()};
	
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
	
	private CoinData coinData;
	
	private Color lineColor;
	private Color fillColor;
	private int thickness;
	
	private Color lineColorSelect;
	private Color fillColorSelect;
	private int thicknessSelect;
	
	private ArrayList<JButton> thickButtonList;
	private ArrayList<JButton> lineColorButtonList;
	private ArrayList<JButton> fillColorButtonList;
	private JButton selectedLineColorButton = null;
	private JButton selectedFillColorButton = null;
	private JButton selectedThickButton = null;
	
	private JLabel lineColorLabel;
	private JLabel fillColorLabel;
	
	private ArrayList<CoinImageButton> iconButtonList;
	private CoinImageButton selectedIconButton;
	
	public ToolAttributePanel(CoinData coinData) {
		this.setBackground(CoinColor.WHITE);
		this.setLayout(null);
		
		this.coinData = coinData;
		
		setup();
	}
	
	public void setup() {
		setupPrimitive();
		setupIcon();
		this.removeAll();
	}
	
	public void setupIcon() {
		iconButtonList = new ArrayList<CoinImageButton>();
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 3; j++) {
				CoinImageButton button = new CoinImageButton();
				button.setIcon(ICON_LIST[j]);
				this.add(button).setBounds(ICON_POSITION[j], ICON_POSITION[i], 50, 50);
				button.addActionListener(this);
				iconButtonList.add(button);
			}
		}
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
			button.setBackground(COLOR_LIST[i]);
			button.setBorder(null);
			this.add(button).setBounds(LINE_COLOR_X + (i * COLOR_PADDING), LINE_COLOR_Y, COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE);
			button.addActionListener(this);
			lineColorButtonList.add(button);
			button.setBorder(null);
			
			if(i == 0) {
				this.lineColor = COLOR_LIST[0];
				selectedLineColorButton = button;
				button.setBorder(new LineBorder(CoinColor.ORANGE, 3));
			}
			
			button = new JButton();
			button.setBackground(COLOR_LIST[i]);
			button.setBorder(null);
			this.add(button).setBounds(FILL_COLOR_X + (i * COLOR_PADDING), FILL_COLOR_Y, COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE);
			button.addActionListener(this);
			fillColorButtonList.add(button);
			button.setBorder(null);
		}
		this.coinData.getDrawingObject().setThickness(thickness);
		this.coinData.getDrawingObject().setLineColor(lineColor);
		this.coinData.getDrawingObject().setFillColor(null);
	}
	
	public void showAttributes() {
		this.removeAll();
		switch(coinData.getDrawingObject().getToolMode()) {
		case SELECT:
			select();
			break;
		case LINE:
			primitives();
			break;
		case RECT:
			primitives();
			break;
		case CIRCLE:
			primitives();
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
	
	private void primitives() {
		for(int i = 0; i < 4; i++) {
			ThickLabel label = new ThickLabel(1 + (i * 2));
			this.add(label).setBounds(THICK_LABEL_X, THICK_LABEL_Y + (i * THICK_PADDING), THICK_LABEL_WIDTH, THICK_LABEL_HEIGHT);
			this.add(thickButtonList.get(i)).setBounds(THICK_BUTTON_X, THICK_BUTTON_Y + (i * THICK_PADDING), THICK_BUTTON_SIZE, THICK_BUTTON_SIZE);
			this.add(lineColorButtonList.get(i)).setBounds(LINE_COLOR_X + (i * COLOR_PADDING), LINE_COLOR_Y, COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE);
			this.add(lineColorLabel);
			if(thickness == 1 + (i * 2))
				thickButtonList.get(i).setBackground(CoinColor.ORANGE);
			else
				thickButtonList.get(i).setBackground(null);
			if(lineColor.getRGB() == COLOR_LIST[i].getRGB())
				lineColorButtonList.get(i).setBorder(new LineBorder(CoinColor.ORANGE, 3));
			else
				lineColorButtonList.get(i).setBorder(null);
			if(coinData.getDrawingObject().getToolMode() != ToolMode.LINE) {
				this.add(fillColorLabel);
				this.add(fillColorButtonList.get(i)).setBounds(FILL_COLOR_X + (i * COLOR_PADDING), FILL_COLOR_Y, COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE);
				if(fillColor != null) {
					if(fillColor.getRGB() == COLOR_LIST[i].getRGB())
						fillColorButtonList.get(i).setBorder(new LineBorder(CoinColor.ORANGE, 3));
					else
						fillColorButtonList.get(i).setBorder(null);
				}
				else
					fillColorButtonList.get(i).setBorder(null);
			}
		}
	}
	
	private void select() {
		if(coinData.getSelectedObject() != null) {
			lineColorSelect = coinData.getSelectedObject().getLineColor();
			fillColorSelect = coinData.getSelectedObject().getFillColor();
			thicknessSelect = coinData.getSelectedObject().getThickness();
			for(int i = 0; i < 4; i++) {
				ThickLabel label = new ThickLabel(1 + (i * 2));
				this.add(label).setBounds(THICK_LABEL_X, THICK_LABEL_Y + (i * THICK_PADDING), THICK_LABEL_WIDTH, THICK_LABEL_HEIGHT);
				this.add(thickButtonList.get(i)).setBounds(THICK_BUTTON_X, THICK_BUTTON_Y + (i * THICK_PADDING), THICK_BUTTON_SIZE, THICK_BUTTON_SIZE);
				this.add(lineColorButtonList.get(i)).setBounds(LINE_COLOR_X + (i * COLOR_PADDING), LINE_COLOR_Y, COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE);
				this.add(lineColorLabel);
				if(thicknessSelect == 1 + (i * 2))
					thickButtonList.get(i).setBackground(CoinColor.ORANGE);
				else
					thickButtonList.get(i).setBackground(null);
				if(lineColorSelect.getRGB() == COLOR_LIST[i].getRGB())
					lineColorButtonList.get(i).setBorder(new LineBorder(CoinColor.ORANGE, 3));
				else
					lineColorButtonList.get(i).setBorder(null);
				if(coinData.getSelectedObject().getToolMode() != ToolMode.LINE) {
					this.add(fillColorLabel);
					this.add(fillColorButtonList.get(i)).setBounds(FILL_COLOR_X + (i * COLOR_PADDING), FILL_COLOR_Y, COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE);
					if(fillColorSelect != null) {
						if(fillColorSelect.getRGB() == COLOR_LIST[i].getRGB())
							fillColorButtonList.get(i).setBorder(new LineBorder(CoinColor.ORANGE, 3));
						else
							fillColorButtonList.get(i).setBorder(null);
					}
					else
						fillColorButtonList.get(i).setBorder(null);
				}
			}
		}
	}
	
	private void icon() {
		for(int i = 0; i < iconButtonList.size(); i++) {
			this.add(iconButtonList.get(i));
		}
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
	
	public void addListener(ActionListener listener) {
		for(int i = 0; i < 4; i++) {
			thickButtonList.get(i).addActionListener(listener);
			lineColorButtonList.get(i).addActionListener(listener);
			fillColorButtonList.get(i).addActionListener(listener);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int thicknessTemp = 0;
		Color lineColorTemp = null;
		Color fillColorTemp = null;
		
		for(int i = 0; i < 4; i++) {
			if(e.getSource() == thickButtonList.get(i)) {
				thicknessTemp = 1 + (i * 2);
				break;
			}
			else if(e.getSource() == lineColorButtonList.get(i)) {
				lineColorTemp = COLOR_LIST[i];
				break;
			}
			else if(e.getSource() == fillColorButtonList.get(i)) {
				fillColorTemp = COLOR_LIST[i];
				break;
			}
		}
		if(coinData.getDrawingObject().getToolMode() == ToolMode.SELECT) {
			if(thicknessTemp != 0) thicknessSelect = thicknessTemp;
			if(lineColorTemp != null) lineColorSelect = lineColorTemp;
			if(fillColorTemp != null) fillColorSelect = fillColorTemp;
			coinData.getSelectedObject().setThickness(thicknessSelect);
			coinData.getSelectedObject().setLineColor(lineColorSelect);
			coinData.getSelectedObject().setFillColor(fillColorSelect);
		}
		else {
			if(thicknessTemp != 0) thickness = thicknessTemp;
			if(lineColorTemp != null) lineColor = lineColorTemp;
			if(fillColorTemp != null) fillColor = fillColorTemp;
			coinData.getDrawingObject().setThickness(thickness);
			coinData.getDrawingObject().setLineColor(lineColor);
			coinData.getDrawingObject().setFillColor(fillColor);
		}
		this.showAttributes();
	}

	public Color getLineColor() {
		return lineColor;
	}

	public Color getFillColor() {
		return fillColor;
	}
	
	public int getThickness() {
		return thickness;
	}
	
}
