package gui.select;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import dataObjects.CoinFloor;
import gui.component.CoinButton;
import gui.component.CoinScrollPane;
import resource.CoinColor;
import resource.CoinFont;

public class MapPreviewPanel extends JPanel {

	private JTextField nameTextField;
	private JTextArea infoTextArea;
	
	private CoinButton editButton;
	private boolean editSwitch = false;
	
	public MapPreviewPanel() {
		this.setBackground(Color.WHITE);
		this.setBorder(new LineBorder(CoinColor.DARK_GRAY, 2));
		this.setLayout(null);
		
		addComponents();
	}
	
	public void addComponents() {
		nameTextField = new JTextField();
		nameTextField.setFont(CoinFont.VERY_BIG_FONT);
		nameTextField.setForeground(CoinColor.DARK_GRAY);
		nameTextField.setBackground(CoinColor.WHITE);
		nameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		nameTextField.setBorder(null);
		nameTextField.setEditable(false);
		this.add(nameTextField).setBounds(10, 10, 710, 100);
		
		infoTextArea = new JTextArea();
		infoTextArea.setFont(CoinFont.BIG_FONT_2);
		//infoTextArea.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, CoinColor.BLACK));
		infoTextArea.setEditable(false);
		//this.add(infoTextArea).setBounds(20, 150, 690, 200);
		
		CoinScrollPane scroll = new CoinScrollPane(infoTextArea);
		scroll.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, CoinColor.BLACK));
		this.add(scroll).setBounds(20, 150, 690, 200);
		scroll.setVisible(true);
		scroll.repaint();
		this.repaint();
		
		editButton = new CoinButton("수정");
		this.add(editButton).setBounds(10, 540, 100, 30);
		editButton.setVisible(false);
	}
	
	public CoinButton getSwitchButon() {
		return this.editButton;
	}
	
	public void addListener(ActionListener actionListener) {
		editButton.addActionListener(actionListener);
	}
	
	public boolean isSwitchOn() {
		return this.editSwitch;
	}
	
	public void switchOn() {
		editButton.setBackground(CoinColor.ORANGE);
		nameTextField.setEditable(true);
		infoTextArea.setEditable(true);
		editSwitch = true;
		editButton.setText("수정 완료");
	}
	
	public void switchOff(CoinFloor floor) {
		editButton.setBackground(CoinColor.THEME_COLOR);
		nameTextField.setEditable(false);
		infoTextArea.setEditable(false);
		editSwitch = false;
		editButton.setText("수정");
	}
	
	public void saveData(CoinFloor floor) {
		/* TODO save changed information to server
		 * 
		 */
		floor.setName(nameTextField.getText().toString());
		floor.setDescription(infoTextArea.getText().toString());
	}
	
	/**
	 * update current contents to selected building information
	 */
	public void updatePreview(CoinFloor floor) {
		/* TODO get building information from server
		 * 1. request building information to server (by building id)
		 * 2. get building information
		 * 3. show the contents on the right screen(preview panel)
		 */
		if(floor == null) {
			nameTextField.setText("");
			infoTextArea.setText("");
		}
		else {
			nameTextField.setText(floor.getName());
			infoTextArea.setText(floor.getDescription());
		}
		
		this.repaint();
	}
}
