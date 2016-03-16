package gui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class CoinEditorFrame extends JFrame implements MouseListener, MouseMotionListener{

	protected int width;
	protected int height;
	protected int x;
	protected int y;
	protected int tempX;
	protected int tempY;
	
	public CoinEditorFrame(String title, int width, int height) {
		super(title);
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.width = width;
		this.height = height;
		x = screen.width / 2 - this.width/2;
		y = screen.height / 2 - this.height/2;
		
		this.setLayout(null);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		//this.setUndecorated(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.getRootPane().setBorder(new LineBorder(Color.BLACK, 2));
		this.getContentPane().setBackground(Color.WHITE);
		this.setIconImage(new ImageIcon("resources/images/Mark_Transparant.png").getImage());
		this.setSize(width, height);
		this.setLocation(x, y);
		this.setResizable(false);
		this.setVisible(true);
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
