package gui.draw;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import gui.component.CoinButton;
import gui.component.CoinFrame;
import resource.CoinColor;
import resource.CoinFont;

public class ExitCheckFrame extends CoinFrame implements ActionListener {
	
	private CoinButton exitWithSaveButton;
	private CoinButton exitWithoutSaveButton;
	
	public ExitCheckFrame(String title, int width, int height) {
		super(title, width, height);
		addComponents();
	}
	
	public ExitCheckFrame() {
		super("CoinExitCheckFrame", 300, 160);
		addComponents();
	}

	public void addComponents() {
		this.exitButton.addActionListener(this);
		this.minimizeButton.addActionListener(this);
		
		addButtons();
	}
	
	public void addButtons() {
		exitWithSaveButton = new CoinButton("������� ���� �� ����");
		this.add(exitWithSaveButton).setBounds(50, 50, 200, 30);
		
		exitWithoutSaveButton = new CoinButton("������� ��� �� ����");
		exitWithoutSaveButton.setBackground(CoinColor.ORANGE);
		this.add(exitWithoutSaveButton).setBounds(50, 100, 200, 30);
	}
	
	public void addListener(ActionListener listener) {
		exitWithSaveButton.addActionListener(listener);
		exitWithoutSaveButton.addActionListener(listener);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == exitButton) {
			dispose();
		}
		else if(e.getSource() == minimizeButton) {
		}
	}
	
	public CoinButton getExitWithSaveButton() {
		return this.exitWithSaveButton;
	}
	
	public CoinButton getExitWithoutSaveButton() {
		return this.exitWithoutSaveButton;
	}
}
