package gui.selector;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import dataObjects.CoinFloor;
import resource.CoinColor;
import resource.CoinFont;

public class MapPreviewPanel extends JPanel {

	private JTextField nameTextField;
	private JTextArea infoTextArea;
	
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
		nameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		nameTextField.setBorder(null);
		this.add(nameTextField).setBounds(10, 10, 710, 100);
		
		infoTextArea = new JTextArea();
		infoTextArea.setFont(CoinFont.BIG_FONT_2);
		infoTextArea.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, CoinColor.BLACK));
		this.add(infoTextArea).setBounds(20, 150, 690, 200);
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
		nameTextField.setText(floor.getName());
		infoTextArea.setText(floor.getBriefInfo());
		
		this.repaint();
	}
}
