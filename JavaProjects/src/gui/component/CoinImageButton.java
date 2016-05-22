package gui.component;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import resource.CoinColor;

public class CoinImageButton extends JButton{

	ImageIcon defaultIcon;
	ImageIcon pressedIcon;
	ImageIcon selectedIcon;
	
	public CoinImageButton() {
		addComponents();
	}
	
	public CoinImageButton(String iconPath) {
		addComponents();
		setIcons(iconPath);
	}
	
	public void setIcons(String iconPath) {
		defaultIcon = new ImageIcon(iconPath + "_Default.png");
		pressedIcon = new ImageIcon(iconPath + "_Pressed.png");
		selectedIcon = new ImageIcon(iconPath + "_Selected.png");
		
		this.setIcon(defaultIcon);
		this.setPressedIcon(pressedIcon);
	}
	
	public void changeIcon() {
		if(this.getIcon() == defaultIcon) {
			this.setIcon(selectedIcon);
		}
		else
			this.setIcon(defaultIcon);
	}
	
	public void addComponents() {
		this.setBackground(CoinColor.WHITE);
		this.setBorder(null);
	}
}
