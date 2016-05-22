package gui.component;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import resource.CoinColor;

public class CoinImageButton extends JButton{

	public CoinImageButton() {
		addComponents();
	}
	
	public CoinImageButton(String iconPath) {
		addComponents();
		setIcons(iconPath);
	}
	
	public void setIcons(String iconPath) {
		this.setIcon(new ImageIcon(iconPath + "_Default.png"));
		this.setPressedIcon(new ImageIcon(iconPath + "_Pressed.png"));
	}
	
	public void addComponents() {
		this.setBackground(CoinColor.WHITE);
		this.setBorder(null);
	}
}
