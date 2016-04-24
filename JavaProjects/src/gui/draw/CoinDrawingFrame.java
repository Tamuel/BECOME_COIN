package gui.draw;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import gui.component.CoinFrame;
import resource.CoinFont;

public class CoinDrawingFrame extends CoinFrame implements ActionListener{
	
	private JLabel markLabel;
	private JLabel nameLabel;

	public CoinDrawingFrame() {
		super("Coin Drawing Frame", 1280, 800);
		
		addComponents();
	}
	
	public void addComponents() {
		addLogo();
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == exitButton) {
			dispose();
		}
		else if(e.getSource() == minimizeButton) {
			setState(Frame.ICONIFIED);
		}
	}

}
