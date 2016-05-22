package gui.draw;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import gui.component.CoinImageButton;
import resource.CoinColor;

public class CoinToolPanel extends JPanel{

	private CoinImageButton lineButton;
	private CoinImageButton rectButton;
	private CoinImageButton circleButton;
	private CoinImageButton iconButton;
	private CoinImageButton tagButton;
	private CoinImageButton beaconButton;
	
	public CoinToolPanel() {
		this.setBackground(CoinColor.WHITE);
		this.setLayout(null);
		
		addComponents();
	}
	
	public void addComponents() {
		lineButton = new CoinImageButton("resources/images/lineButton");
		this.add(lineButton).setBounds(30, 30, 50, 50);
		
		rectButton = new CoinImageButton("resources/images/rectButton");
		this.add(rectButton).setBounds(120, 30, 50, 50);
		
		circleButton = new CoinImageButton("resources/images/circleButton");
		this.add(circleButton).setBounds(30, 110, 50, 50);
		
		iconButton = new CoinImageButton("resources/images/iconButton");
		this.add(iconButton).setBounds(120, 110, 50, 50);
		
		tagButton = new CoinImageButton("resources/images/tagButton");
		this.add(tagButton).setBounds(30, 190, 50, 50);
		
		beaconButton = new CoinImageButton("resources/images/beaconButton");
		this.add(beaconButton).setBounds(120, 190, 50, 50);
	}
}
