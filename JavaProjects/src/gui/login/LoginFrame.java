package gui.login;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import gui.component.CoinButton;
import gui.component.CoinFrame;
import gui.component.CoinPasswordField;
import gui.component.CoinTextField;
import resource.CoinColor;

public class LoginFrame extends CoinFrame{
	
	private CoinTextField idField;
	private CoinPasswordField passwordField;

	private CoinButton loginButton;
	private CoinButton exitButton;
	
	private int xBorder = 10;
	private int yBorder = 10;
	

	public LoginFrame(String frameName, int width, int height) {
		super(frameName, width, height);

		idField = new CoinTextField("ID");
		idField.setBackground(CoinColor.LIGHT_GRAY);
		idField.setSize(this.getWidth() - xBorder * 2, this.getHeight() / 4);
		idField.setLocation(xBorder, yBorder * 2);

		passwordField = new CoinPasswordField("PASSWORD");
		passwordField.setBackground(CoinColor.LIGHT_GRAY);
		passwordField.setSize(this.getWidth() - xBorder * 2, this.getHeight() / 4);
		passwordField.setLocation(xBorder, yBorder * 3 + this.getHeight() / 4);
		
		loginButton = new CoinButton("로그인");//StringR.LOGIN);
		loginButton.setSize((this.getWidth() - xBorder * 3) / 2, this.getHeight() / 4);
		loginButton.setLocation(xBorder, yBorder * 4 + this.getHeight() / 4 * 2);
		loginButton.addActionListener(new ButtonListener());
		
		exitButton = new CoinButton("나가기");//StringR.EXIT);
		exitButton.setSize((this.getWidth() - xBorder * 3) / 2, this.getHeight() / 4);
		exitButton.setLocation(xBorder * 2 + loginButton.getWidth(), yBorder * 4 + this.getHeight() / 4 * 2);
		exitButton.addActionListener(new ButtonListener());
		
		this.add(idField);
		this.add(passwordField);
		this.add(loginButton);
		this.add(exitButton);
		
		getContentPane().setBackground(CoinColor.WHITE);
		this.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev){
			/*switch(ev.getSource().toString()) {
			case "로그인":
				DataProvider.getInstance();
				DataProvider.getInstance().setId(idField.getText());
				new SelectFloorPlanFrame(
						StringR.SELECT_BUILDING,
						DimenR.BUILDING_LIST_FRAME_WIDTH,
						DimenR.BUILDING_LIST_FRAME_HEIGHT
						);
                dispose();
				break;
				
			case "나가기":
                dispose();
                System.exit(0);
				break;
			}*/
		}
	}
}
