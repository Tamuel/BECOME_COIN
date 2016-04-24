package gui.selector;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gui.component.CoinButton;
import gui.component.CoinFrame;
import gui.login.LoginFrame;
import gui.login.SignupFrame;
import resource.CoinFont;

public class SelectionFrame extends CoinFrame implements ActionListener{
	
	private JLabel markLabel;
	private JLabel nameLabel;
	private JLabel userNameLabel;
	
	private JPanel mapListPanel;
	private JPanel mapPreviewPanel;
	
	private CoinButton logoutButton;
	
	public SelectionFrame() {
		super("Selection Frame", 1280, 768);
		
		addComponents();
	}
	
	public void addComponents() {
		addLogo();
		addPanels();
		
		// TODO get logged in user name(id) from server if it's valid
		String userName = "Park";
		userNameLabel = new JLabel(userName + " ´Ô");
		userNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		userNameLabel.setFont(CoinFont.BIG_FONT);
		this.add(userNameLabel).setBounds(1030, 50, 200, 30);
		
		logoutButton = new CoinButton("·Î±×¾Æ¿ô");
		logoutButton.addActionListener(this);
		this.add(logoutButton).setBounds(1130, 90, 100, 30);
	}
	
	public void addPanels() {
		mapListPanel = new MapListPanel();
		this.add(mapListPanel).setBounds(50, 140, 400, 580);
		
		mapPreviewPanel = new MapPreviewPanel();
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
	}
}
