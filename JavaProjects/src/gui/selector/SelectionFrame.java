package gui.selector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import dataObjects.CoinFloor;
import gui.component.CoinButton;
import gui.component.CoinFrame;
import gui.component.CoinScrollPane;
import gui.login.LoginFrame;
import gui.login.SignupFrame;
import resource.CoinColor;
import resource.CoinFont;

public class SelectionFrame extends CoinFrame implements ActionListener{
	
	private JLabel markLabel;
	private JLabel nameLabel;
	private JLabel userNameLabel;
	
	private MapListPanel mapListPanel;
	private MapPreviewPanel mapPreviewPanel;
	
	private CoinButton logoutButton;
	private CoinButton selectedButton = null;
	private CoinButton newButton;
	private CoinButton deleteButton;
	private CoinButton editButton;
	private CoinButton upButton;
	private CoinButton downButton;
	
	private CoinScrollPane scroll;
	
	private ArrayList<CoinFloor> floorList;
	private CoinFloor selectedFloor = null;
	
	public SelectionFrame() {
		super("Selection Frame", 1280, 800);
		
		loadFloors();
		
		addComponents();
	}
	
	public void addComponents() {
		addLogo();
		addPanels();
		
		// TODO get logged in user name(id) from server if it's valid
		String userName = "Park, Dongwon";
		userNameLabel = new JLabel(userName + " 님");
		userNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		userNameLabel.setFont(CoinFont.BIG_FONT);
		this.add(userNameLabel).setBounds(1030, 50, 200, 30);
		
		logoutButton = new CoinButton("로그아웃");
		logoutButton.addActionListener(this);
		this.add(logoutButton).setBounds(1130, 90, 100, 30);
		
		newButton = new CoinButton("추가");
		newButton.addActionListener(this);
		this.add(newButton).setBounds(240, 730, 100, 30);
		
		deleteButton = new CoinButton("삭제");
		deleteButton.addActionListener(this);
		this.add(deleteButton).setBounds(367, 730, 100, 30);
		
		editButton = new CoinButton("도면 수정");
		editButton.addActionListener(this);
		this.add(editButton).setBounds(1129, 730, 100, 30);
		
		upButton = new CoinButton("△");
		upButton.addActionListener(this);
		upButton.setBackground(CoinColor.ORANGE);
		this.add(upButton).setBounds(50 ,730, 30, 30);
		
		downButton = new CoinButton("▽");
		downButton.addActionListener(this);
		downButton.setBackground(CoinColor.ORANGE);
		this.add(downButton).setBounds(90 ,730, 30, 30);
	}
	
	/**
	 * load floor data from server
	 * <br>except drawing objects for performance and traffic
	 */
	public void loadFloors() {
		floorList = new ArrayList<CoinFloor>();
		
		for(int i = 0; i < 10; i++) {
			CoinFloor floor = new CoinFloor();
			floor.setName("KNU Global Plaza floor " + (i + 1));
			floor.setBriefInfo("This is floor #" + (i+1) + " of KNU GP");
			floor.setOwner("KNU");
			floorList.add(floor);
		}
	}
	
	public void addPanels() {		
		mapListPanel = new MapListPanel(this);
		mapListPanel.makeList(floorList);
		scroll = new CoinScrollPane(mapListPanel);
		scroll.setBorder(new LineBorder(CoinColor.DARK_GRAY, 2));
		this.add(scroll).setBounds(50, 140, 417, 580);
		
		mapListPanel.repaint();
		scroll.repaint();
		
		mapPreviewPanel = new MapPreviewPanel();
		mapPreviewPanel.addListener(this);
		this.add(mapPreviewPanel).setBounds(500, 140, 730, 580);
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
	
	private void buttonColorChange() {
		if(selectedButton != null) {
			selectedButton.setBackground(CoinColor.WHITE);
			selectedButton.setForeground(CoinColor.BLACK);
		}
		if(selectedFloor != null) {
			int index = floorList.indexOf(selectedFloor);
			selectedButton = mapListPanel.getListButton().get(index);
			selectedButton.setBackground(CoinColor.ORANGE);
			selectedButton.setForeground(CoinColor.WHITE);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == exitButton) {
			dispose();
		}
		else if(e.getSource() == minimizeButton) {
			setState(Frame.ICONIFIED);
		}
		else if(e.getSource() == logoutButton) {
			/* TODO logout process must be implemented
			 * 1. save all current data, status and changed information to the server DB
			 * 2. empty all saved data, status and information in the local memory
			 * 3. dispose this frame and open the login frame (done)
			 */
			LoginFrame loginFrame = new LoginFrame();
			this.dispose();
		}
		else if(e.getSource() == newButton) {
			/* TODO new floor adding
			 * 
			 */
			CoinFloor floor = new CoinFloor();
			floor.setName("new floor");
			floor.setBriefInfo("this is new added floor.");
			floorList.add(floor);
			mapListPanel.makeList(floorList);
			buttonColorChange();
			this.repaint();
		}
		else if(e.getSource() == deleteButton) {
			/* TODO delete floor information
			 * 
			 */
			System.out.println(selectedFloor.getName() + " is deleted");
			int index = floorList.indexOf(selectedFloor);
			floorList.remove(index);
			mapListPanel.makeList(floorList);
			mapPreviewPanel.updatePreview(null);
		}
		else if(e.getSource() == editButton) {
			/* TODO edit floor plan(map) of selected floor
			 * 
			 */
		}
		else if(e.getSource() == mapPreviewPanel.getSwitchButon()) {
			if(mapPreviewPanel.isSwitchOn() == true) {
				mapPreviewPanel.switchOff(selectedFloor);
				mapPreviewPanel.saveData(selectedFloor);
				mapListPanel.makeList(floorList);
			}
			else {
				mapPreviewPanel.switchOn();
			}
		}
		else if(e.getSource() == upButton) {
			/* TODO change server data
			 * 
			 */
			if(selectedFloor != null) {
				int index = floorList.indexOf(selectedFloor);
				if(index != 0) {
					CoinFloor temp = floorList.get(index - 1);
					floorList.set(index - 1, selectedFloor);
					floorList.set(index, temp);
				}
				mapListPanel.makeList(floorList);
				this.buttonColorChange();
			}
		}
		else if(e.getSource() == downButton) {
			/* TODO change server data
			 * 
			 */
			if(selectedFloor != null) {
				int index = floorList.indexOf(selectedFloor);
				if(index != floorList.size() - 1) {
					CoinFloor temp = floorList.get(index + 1);
					floorList.set(index + 1, selectedFloor);
					floorList.set(index, temp);
				}
				mapListPanel.makeList(floorList);
				this.buttonColorChange();
			}
		}
		else {
			for(int i = 0; i < mapListPanel.getListButton().size(); i++) {
				if(e.getSource() == mapListPanel.getListButton().get(i)) {
					selectedFloor = floorList.get(i);
					mapPreviewPanel.switchOff(selectedFloor);
					mapPreviewPanel.updatePreview(floorList.get(i));
					buttonColorChange();
					System.out.println(selectedButton.getText());
					
					this.repaint();
				}
			}
		}
	}
}
