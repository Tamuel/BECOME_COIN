package gui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import resource.CoinColor;

public class CoinFrame extends JFrame implements MouseListener, MouseMotionListener{

	protected int width;
	protected int height;
	protected int x;
	protected int y;
	protected int tempX;
	protected int tempY;
	
	protected JButton exitButton;
	protected JButton minimizeButton;
	
	public CoinFrame(String title, int width, int height) {
		super(title);
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.width = width;
		this.height = height;
		x = screen.width / 2 - this.width/2;
		y = screen.height / 2 - this.height/2;
		
		this.setLayout(null);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setUndecorated(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getRootPane().setBorder(new LineBorder(CoinColor.DARK_GRAY, 2));
		this.getContentPane().setBackground(Color.WHITE);
		this.setIconImage(new ImageIcon("resources/images/Mark_Transparant.png").getImage());
		this.setSize(width, height);
		this.setLocation(x, y);
		this.setResizable(false);
		this.setVisible(true);
		
		exitButton = new JButton("x");
		exitButton.setBackground(Color.WHITE);
		exitButton.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 20));
		exitButton.setBorder(null);
		exitButton.setOpaque(true);
		exitButton.setFocusPainted(false);
		this.add(exitButton).setBounds(width - 40, 0, 30, 30);
		
		minimizeButton = new JButton("-");
		minimizeButton.setBackground(Color.WHITE);
		minimizeButton.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 26));
		minimizeButton.setBorder(null);
		minimizeButton.setOpaque(true);
		minimizeButton.setFocusPainted(false);
		this.add(minimizeButton).setBounds(width - 80, 0, 30, 30);
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mouseMoved(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
		tempX = arg0.getX();
		tempY = arg0.getY();
	}

	public void mouseDragged(MouseEvent arg0) {
		x = arg0.getXOnScreen() - tempX;
		y = arg0.getYOnScreen() - tempY;
		
		this.setBounds(x, y, width, height);
	}
	
}
