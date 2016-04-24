package gui.selector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import dataObjects.CoinFloor;
import gui.component.CoinButton;
import resource.CoinColor;
import resource.CoinFont;

public class MapListPanel extends JPanel{

	private ActionListener actionListener;
	private ArrayList<CoinButton> mapListButton;
	
	public MapListPanel(ActionListener actionListener) {
		this.setBackground(CoinColor.LIGHT_GRAY);
		this.setLayout(null);
		
		this.actionListener = actionListener;
		
		mapListButton = new ArrayList<CoinButton>();
		
		addComponents();
	}
	
	public void addComponents() {
		//makeList();
	}
	
	public void makeList(ArrayList<CoinFloor> floorList) {
		this.removeAll();
		mapListButton.clear();
		for(int i = 0; i < floorList.size(); i++) {
			
			CoinButton mapButton = new CoinButton(floorList.get(i).getName());
			mapButton.setBackground(CoinColor.WHITE);
			mapButton.setForeground(Color.BLACK);
			mapButton.setFont(CoinFont.BIG_FONT_2);
			mapButton.addActionListener(actionListener);
			mapListButton.add(mapButton);
			this.add(mapButton).setBounds(0, (104 * i), 400, 102);
		}
		
		this.setPreferredSize(new Dimension(400, 104 * mapListButton.size()));
		
		this.repaint();
	}
	
	public void addFloor(CoinFloor floor) {
		CoinButton mapButton = new CoinButton(floor.getName());
		mapButton.setBackground(CoinColor.WHITE);
		mapButton.setForeground(Color.BLACK);
		mapButton.setFont(CoinFont.BIG_FONT_2);
		mapButton.addActionListener(actionListener);
		mapListButton.add(mapButton);
		this.add(mapButton).setBounds(0, (104 * (mapListButton.size() - 1)), 400, 102);
		
		this.setPreferredSize(new Dimension(400, 104 * mapListButton.size()));
		//this.repaint();
	}
	
	public ArrayList<CoinButton> getListButton() {
		return this.mapListButton;
	}
}
