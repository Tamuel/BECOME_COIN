package gui.draw;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import gui.component.CoinFrame;
import resource.CoinColor;
import resource.CoinFont;

public class CoinDrawingFrame extends CoinFrame implements ActionListener{
	
	private CoinCanvasPanel coinCanvasPanel;
	private CoinToolPanel coinToolPanel;
	private CoinToolAttribPanel coinToolAttribPanel;
	
	private JLabel markLabel;
	private JLabel nameLabel;

	public CoinDrawingFrame() {
		super("Coin Drawing Frame", 1280, 800);
		
		addComponents();
	}
	
	public void addComponents() {
		addLogo();
		addPanels();
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
		coinCanvasPanel = new CoinCanvasPanel();
		coinCanvasPanel.setBorder(new LineBorder(CoinColor.DARK_GRAY, 2));
		this.add(coinCanvasPanel).setBounds(50, 140, 950, 580);
		
		coinToolPanel = new CoinToolPanel();
		coinToolPanel.setBorder(new LineBorder(CoinColor.DARK_GRAY, 2));
		this.add(coinToolPanel).setBounds(1033, 140, 200, 280);
		
		coinToolAttribPanel = new CoinToolAttribPanel();
		coinToolAttribPanel.setBorder(new LineBorder(CoinColor.DARK_GRAY, 2));
		this.add(coinToolAttribPanel).setBounds(1033, 440, 200, 280);
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
