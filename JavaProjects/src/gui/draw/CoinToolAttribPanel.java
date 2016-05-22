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
import javax.swing.border.LineBorder;

import resource.CoinColor;

public class CoinToolAttribPanel extends JPanel implements ActionListener{
	
	private static final Color[] colorList = {CoinColor.BLACK, CoinColor.THEME_COLOR, CoinColor.OBJECT_COLOR_1, CoinColor.OBJECT_COLOR_2};
	
	private Color color;
	private int thickness;
	
	private ArrayList<JButton> thickButtonList;
	private ArrayList<JButton> colorButtonList;
	private JButton selectedColorButton = null;
	private JButton selectedThickButton = null;
	
	public CoinToolAttribPanel() {
		this.setBackground(CoinColor.WHITE);
		this.setLayout(null);
		
		thickButtonList = new ArrayList<JButton>();
		colorButtonList = new ArrayList<JButton>();
		
		for(int i = 0; i < 4; i++) {
			ThickLabel label = new ThickLabel(1 + (i * 2));
			this.add(label).setBounds(50, 30 + (i * 40), 130, 50);
			
			JButton button = new JButton();
			button.setBackground(null);
			this.add(button).setBounds(20, 20 + (40* i), 20, 20);
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
			this.add(button).setBounds(25 + (40 * i), 200, 30, 30);
			button.addActionListener(this);
			colorButtonList.add(button);
			
			if(i == 0) {
				this.color = colorList[0];
				selectedColorButton = button;
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
			this.add(label).setBounds(50, 30 + (i * 40), 130, 50);
			this.add(thickButtonList.get(i)).setBounds(20, 20 + (40* i), 20, 20);
			this.add(colorButtonList.get(i)).setBounds(25 + (40 * i), 200, 30, 30);
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
			if(e.getSource() == colorButtonList.get(i)) {
				this.color = colorList[i];
				if(selectedColorButton != null)
					selectedColorButton.setBorder(null);
				selectedColorButton = colorButtonList.get(i);
				selectedColorButton.setBorder(new LineBorder(CoinColor.ORANGE, 3));
				break;
			}
		}
	}
}
