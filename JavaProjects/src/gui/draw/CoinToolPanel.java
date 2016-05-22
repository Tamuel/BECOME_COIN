package gui.draw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import gui.component.CoinImageButton;
import resource.CoinColor;

public class CoinToolPanel extends JPanel implements ActionListener {
	
	private static final int LEFT = 30;
	private static final int RIGHT = 120;
	
	private CoinImageButton selectButton;
	private CoinImageButton lineButton;
	private CoinImageButton rectButton;
	private CoinImageButton circleButton;
	private CoinImageButton iconButton;
	private CoinImageButton tagButton;
	private CoinImageButton beaconButton;
	
	private ArrayList<CoinImageButton> buttonList;
	private ArrayList<ImageIcon> buttonIcon;
	
	private CoinImageButton selectedButton;
	
	private CoinToolMode toolMode = null;
	
	public CoinToolPanel() {
		this.setBackground(CoinColor.WHITE);
		this.setLayout(null);
		
		addComponents();
	}
	
	public void addComponents() {
		buttonList = new ArrayList<CoinImageButton>();
		selectedButton = null;
		
		selectButton = new CoinImageButton("resources/images/selectButton");
		this.add(selectButton).setBounds(LEFT, 20, 50, 50);
		buttonList.add(selectButton);
		
		lineButton = new CoinImageButton("resources/images/lineButton");
		this.add(lineButton).setBounds(RIGHT, 20, 50, 50);
		buttonList.add(lineButton);
		
		rectButton = new CoinImageButton("resources/images/rectButton");
		this.add(rectButton).setBounds(LEFT, 90, 50, 50);
		buttonList.add(rectButton);
		
		circleButton = new CoinImageButton("resources/images/circleButton");
		this.add(circleButton).setBounds(RIGHT, 90, 50, 50);
		buttonList.add(circleButton);
		
		iconButton = new CoinImageButton("resources/images/iconButton");
		this.add(iconButton).setBounds(LEFT, 160, 50, 50);
		buttonList.add(iconButton);
		
		tagButton = new CoinImageButton("resources/images/tagButton");
		this.add(tagButton).setBounds(RIGHT, 160, 50, 50);
		buttonList.add(tagButton);
		
		beaconButton = new CoinImageButton("resources/images/beaconButton");
		this.add(beaconButton).setBounds(LEFT, 230, 50, 50);
		buttonList.add(beaconButton);
	}
	
	public void addListener(ActionListener listener) {
		for(int i = 0; i < buttonList.size(); i++) {
			buttonList.get(i).addActionListener(listener);
			buttonList.get(i).addActionListener(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		CoinImageButton currentButton = (CoinImageButton)(e.getSource());
		if(selectedButton != null) {
			selectedButton.changeIcon();
		}
		selectedButton = currentButton;
		currentButton.changeIcon();
		switch(buttonList.indexOf(selectedButton)) {
		case 0:
			this.toolMode = CoinToolMode.SELECT;
			break;
		case 1:
			this.toolMode = CoinToolMode.LINE;
			break;
		case 2:
			this.toolMode = CoinToolMode.RECT;
			break;
		case 3:
			this.toolMode = CoinToolMode.CIRCLE;
			break;
		case 4:
			this.toolMode = CoinToolMode.ICON;
			break;
		case 5:
			this.toolMode = CoinToolMode.TAG;
			break;
		case 6:
			this.toolMode = CoinToolMode.BEACON;
			break;
		}
	}
	
	public CoinToolMode getMode() {
		return this.toolMode;
	}
}
