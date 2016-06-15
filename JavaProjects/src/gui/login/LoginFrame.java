package gui.login;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import gui.component.CoinButton;
import gui.component.CoinFrame;
import gui.component.CoinPasswordField;
import gui.component.CoinTextField;
import gui.select.SelectionFrame;
import resource.CoinColor;
import resource.CoinFont;
import serverConnector.ServerManager;

public class LoginFrame extends CoinFrame implements ActionListener{
	
	private CoinTextField idField;
	private CoinPasswordField passwordField;

	private CoinButton loginButton;
	private CoinButton signupButton;
	
	private JLabel markLabel;
	private JLabel nameLabel;
	
	private int xBorder = 10;
	private int yBorder = 10;

	public LoginFrame() {
		super("Coin Editor Login", 400, 310);
		
		addComponents();
		
		this.repaint();
	}
	
	public void addComponents() {
		idField = new CoinTextField("ID");
		idField.setBackground(CoinColor.LIGHT_GRAY);
		this.add(idField).setBounds(xBorder * 5, yBorder * 12, this.getWidth() - xBorder * 10, 50);

		passwordField = new CoinPasswordField("PASSWORD");
		passwordField.setBackground(CoinColor.LIGHT_GRAY);
		this.add(passwordField).setBounds(xBorder * 5, yBorder * 18, this.getWidth() - xBorder * 10,50);
		
		loginButton = new CoinButton("로그인");//StringR.LOGIN);
		loginButton.addActionListener(this);
		this.add(loginButton).setBounds(xBorder * 5, yBorder * 24, 100, 40);
		
		signupButton = new CoinButton("회원가입");//StringR.EXIT);
		signupButton.addActionListener(this);
		this.add(signupButton).setBounds(this.getWidth() - xBorder * 15, yBorder * 24, 100, 40);
		
		markLabel = new JLabel();
		markLabel.setIcon(new ImageIcon("resources/images/Mark_Transparent_100x100.png"));
		this.add(markLabel).setBounds(xBorder * 7, yBorder * 2, 100, 100);
		
		nameLabel = new JLabel("Coin Editor");
		nameLabel.setFont(CoinFont.VERY_BIG_FONT);
		this.add(nameLabel).setBounds(xBorder * 17, yBorder * 4 + 4, 200, 50);
		
		this.exitButton.addActionListener(this);
		this.minimizeButton.addActionListener(this);
		
		getContentPane().setBackground(CoinColor.WHITE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == loginButton) {
			/* TODO login process must be implemented
			 * 1. request login to server
			 * 2. if valid reply with user data, disopse this and open the selection frame
			 * 3. if not valid with null data, do nothing and ask user to retry
			 */
			String data = ServerManager.getInstance().requestLoginToServer(idField.getText(), passwordField.getText());

	        if(ServerManager.getInstance().getResult() == ServerManager.ACCEPT) {
	            ServerManager.getInstance().setUserNickName(data.split("\n")[0]);
	            ServerManager.getInstance().parseFloorPlanData(data.substring(data.split("\n")[0].length() + 1));
	            ServerManager.getInstance().setUserID(idField.getText());
				SelectionFrame selectionFrame = new SelectionFrame();
				this.dispose();
	        } else {
	        	// Login Fail
	        }
		}
		else if(e.getSource() == signupButton) {
			SignupFrame signupFrame = new SignupFrame();
			this.dispose();
		}
		else if(e.getSource() == exitButton) {
			dispose();
		}
		else if(e.getSource() == minimizeButton) {
			setState(Frame.ICONIFIED);
		}
	}
}
