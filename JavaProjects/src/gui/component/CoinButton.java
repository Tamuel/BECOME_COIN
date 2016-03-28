package gui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import resource.CoinColor;
import resource.CoinFont;

public class CoinButton extends JButton{
	
	JLabel label1;
	JLabel label2;
	JLabel label3;
	
	private Font font = CoinFont.BIG_FONT;
	private Font font2 = CoinFont.SMALL_FONT;
	
	private int adjustPos = -20;

	public CoinButton() {
		super();
		custom();
	}
	
	public CoinButton(String arg) {
		super(arg);
		custom();
	}
	
	public CoinButton(String arg, int width, int height) {
		super(arg);
		custom();
		this.setSize(width, height);
	}
	
	public CoinButton(String arg0, String arg1, int width, int height) {
		super();
		this.setSize(width, height);
		this.setLayout(null);
		custom();
		
		label1 = new JLabel(arg0);
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		label1.setSize(this.getWidth(), 50);
		label1.setLocation(0, height / 3 + adjustPos);
		label1.setFont(font);
		label1.setForeground(Color.WHITE);
		
		label2 = new JLabel(arg1);
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		label2.setSize(this.getWidth(), 40);
		label2.setLocation(0, height * 2 / 3 + adjustPos);
		label2.setFont(font2);
		label2.setForeground(Color.WHITE);
		
		this.add(label1);
		this.add(label2);
	}
	
	public CoinButton(String arg0, String arg1, String arg2, int width, int height) {
		super();
		this.setSize(width, height);
		this.setLayout(null);
		custom();
		
		label1 = new JLabel(arg0);
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		label1.setSize(this.getWidth(), 50);
		label1.setLocation(0, height / 4 + adjustPos);
		label1.setFont(font);
		label1.setForeground(Color.WHITE);
		
		label2 = new JLabel(arg1);
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		label2.setSize(this.getWidth(), 40);
		label2.setLocation(0, height * 3 / 5 + adjustPos);
		label2.setFont(font2);
		label2.setForeground(Color.WHITE);
		
		label3 = new JLabel(arg2);
		label3.setHorizontalAlignment(SwingConstants.CENTER);
		label3.setSize(this.getWidth(), 40);
		label3.setLocation(0, height * 5 / 6 + adjustPos);
		label3.setFont(font2);
		label3.setForeground(Color.WHITE);
		
		this.add(label1);
		this.add(label2);
		this.add(label3);
	}
	
	public void setFontColor(Color a, Color b) {
		label1.setForeground(a);
		label2.setForeground(b);
	}
	
	public void setFontColor(Color a, Color b, Color c) {
		label1.setForeground(a);
		label2.setForeground(b);
		label3.setForeground(c);
	}
	
	private void custom() {
		this.setOpaque(true);
		//this.setBackground(new Color(30, 114, 237));
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setBackground(CoinColor.BUTTON_COLOR);
		Border empty = new EmptyBorder(2, 2, 2, 2);
		this.setBorder(empty);
		this.setFont(font);
		this.setForeground(Color.WHITE);
		this.setFocusPainted(false);
        super.setContentAreaFilled(false);
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
        	g.setColor(getBackground().darker());
        } else if (getModel().isRollover()) {
            g.setColor(getBackground());
        } else {
            g.setColor(getBackground());
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
	
	public String toString() {
		return getText();
	}
}
