package gui.login;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import gui.component.CoinButton;
import gui.component.CoinFrame;
import resource.CoinColor;
import resource.CoinFont;

public class SignupFrame extends CoinFrame implements ActionListener, KeyListener, FocusListener{

	private static final long serialVersionUID = -2859575932051396428L;
	
	private JLabel markLabel;
	private JLabel nameLabel;
	
	private JLabel idLabel;
	private JLabel passwordLabel;
	private JLabel passwordCheckLabel;
	private JLabel warningLabel;
	
	private JTextField idField;
	private JPasswordField passwordField;
	private JPasswordField passwordCheckField;
	
	private CoinButton confirmButton;
	private CoinButton cancelButton;
	
	final char alphabets[] = {
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	final char numbers[] = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	final char specials[] = {
			'~', '!', '@', '#', '$', '%', '^', '&', '*'};
	
	public SignupFrame() {
		super("SignupFrame", 400, 480);
		
		addComponents();
		
		idField.grabFocus();
	}
	
	public void addComponents() {
		this.exitButton.addActionListener(this);
		this.minimizeButton.addActionListener(this);
		
		markLabel = new JLabel();
		markLabel.setIcon(new ImageIcon("resources/images/Mark_Transparent_100x100.png"));
		this.add(markLabel).setBounds(50, 20, 100, 100);
		
		nameLabel = new JLabel("Coin Signup");
		nameLabel.setFont(CoinFont.VERY_BIG_FONT);
		this.add(nameLabel).setBounds(150, 44, 200, 50);
		
		idLabel = new JLabel("아이디");
		idLabel.setFont(CoinFont.BIG_FONT);
		this.add(idLabel).setBounds(55, 140, this.getWidth() - 100, 40);
		
		idField = new JTextField();
		idField.setBorder(new LineBorder(CoinColor.DARK_GRAY, 1));
		idField.setFont(CoinFont.BIG_FONT);
		idField.addKeyListener(this);
		this.add(idField).setBounds(50, 180, this.getWidth() - 100, 40);
		
		passwordLabel = new JLabel("비밀번호");
		passwordLabel.setFont(CoinFont.BIG_FONT);
		this.add(passwordLabel).setBounds(55, 225, this.getWidth() - 100, 40);
		
		passwordField = new JPasswordField();
		passwordField.setBorder(new LineBorder(CoinColor.DARK_GRAY, 1));
		passwordField.setFont(CoinFont.BIG_FONT);
		passwordField.addKeyListener(this);
		this.add(passwordField).setBounds(50, 260, this.getWidth() - 100, 40);
		
		passwordCheckLabel = new JLabel("비밀번호 확인");
		passwordCheckLabel.setFont(CoinFont.BIG_FONT);
		this.add(passwordCheckLabel).setBounds(55, 305, this.getWidth() - 100, 40);
		
		passwordCheckField = new JPasswordField();
		passwordCheckField.setBorder(new LineBorder(CoinColor.DARK_GRAY, 1));
		passwordCheckField.setFont(CoinFont.BIG_FONT);
		passwordCheckField.addKeyListener(this);
		this.add(passwordCheckField).setBounds(50, 340, this.getWidth() - 100, 40);
		
		confirmButton = new CoinButton("회원 가입");
		confirmButton.addActionListener(this);
		this.add(confirmButton).setBounds(50, 400, 100, 40);
		
		cancelButton = new CoinButton("취소");
		cancelButton.addActionListener(this);
		this.add(cancelButton).setBounds(250, 400, 100, 40);
		
		warningLabel = new JLabel("");
		warningLabel.setFont(CoinFont.SMALL_FONT);
		warningLabel.setForeground(CoinColor.RED);
		warningLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(warningLabel).setBounds(0, 120, 400, 20);
	}
	
	/**
	 * 1. id must not be duplicated
	 * 2. password and passwordCheck must be same
	 * 3. password must be longer than 10-word and contain alphabets, numbers, special characters
	 * @return true if id and password is good
	 */
	public boolean validCheck() {
		
		if(idField.getText().isEmpty()) {
			warningLabel.setText("아이디를 입력해주세요.");
			idField.grabFocus();
			return false;
		}
		// TODO id duplication check process must me implemented
		/*else if(id duplication check) {
		 	warningLabel.setText("이미 존재하는 아이디입니다.");
			idField.grabFocus();
			duplicationCheck();
		}*/
		else if(passwordField.getPassword().length == 0) {
			warningLabel.setText("비밀번호를 입력해주세요.");
			passwordField.grabFocus();
			return false;
		}
		else if(passwordValidCheck() == false) {
			warningLabel.setText("비밀번호는 영문자, 숫자, 특수기호 포함 및 10자 이상이어야 합니다.");
			passwordField.grabFocus();
			return false;
		}
		else if(!Arrays.equals(passwordField.getPassword(), passwordCheckField.getPassword())) {
			warningLabel.setText("비밀번호가 다릅니다.");
			passwordCheckField.grabFocus();
			return false;
		}
		
		return true;
	}
	
	private boolean passwordValidCheck() {
		boolean flagAlphabet = false;
		boolean flagNumber = false;
		boolean flagSpecial = false;
		
		if(passwordField.getPassword().length < 10)
			return false;
		
		for(int i = 0; i < passwordField.getPassword().length; i++) {
			char temp = passwordField.getPassword()[i];
			
			for(int j = 0; j < alphabets.length; j++) {
				if(temp == alphabets[j]) {
					flagAlphabet = true;
					break;
				}
			}
			if(flagAlphabet == false)
				return false;
			
			for(int j = 0; j < numbers.length; j++) {
				if(temp == numbers[j]) {
					flagNumber = true;
					break;
				}
			}
			if(flagNumber == false)
				return false;
			
			for(int j = 0; j < specials.length; j++) {
				if(temp == specials[j]) {
					flagSpecial = true;
					break;
				}
			}
			if(flagSpecial == false)
				return false;
		}
		
		return true;
	}
	
	private void signup() {
		if(validCheck() == true) {
			// TODO Signup process must be implemented
			LoginFrame loginFrame = new LoginFrame();
			this.dispose();
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			if(arg0.getSource() == idField)
				passwordField.grabFocus();
			else if(arg0.getSource() == passwordField)
				passwordCheckField.grabFocus();
			else
				signup();
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_UP) {
			if(arg0.getSource() == passwordCheckField)
				passwordField.grabFocus();
			else if(arg0.getSource() == passwordField)
				idField.grabFocus();
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_DOWN) {
			if(arg0.getSource() == idField)
				passwordField.grabFocus();
			else if(arg0.getSource() == passwordField)
				passwordCheckField.grabFocus();
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			LoginFrame loginFrame = new LoginFrame();
			this.dispose();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == exitButton) {
			LoginFrame loginFrame = new LoginFrame();
			this.dispose();
		}
		else if(e.getSource() == minimizeButton) {
			this.setState(Frame.ICONIFIED);
		}
		else if(e.getSource() == confirmButton) {
			signup();
		}
		else if(e.getSource() == cancelButton) {
			LoginFrame loginFrame = new LoginFrame();
			this.dispose();
		}
	}
}
