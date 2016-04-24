package gui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPasswordField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import resource.CoinColor;
import resource.CoinFont;

public class CoinPasswordField extends JPasswordField implements MouseListener, FocusListener, DocumentListener {
	private Font font = CoinFont.BIG_FONT;
	private Font font2 = CoinFont.SMALL_FONT;
	
	private Color fontColor = new Color(180, 180, 180);
	private Color fontColor2 = new Color(120, 120, 120);
	
	private String text;

	public CoinPasswordField() {
		super();
		custom();
	}
	
	public CoinPasswordField(String arg) {
		super(arg);
		custom();
		text = arg;
	}
	
	private void custom() {
		this.setBackground(Color.WHITE);
		Border empty = new EmptyBorder(10, 10, 10, 10);
		this.setBorder(empty);
		this.setFont(font);
		this.setForeground(fontColor);
		this.addFocusListener(this);
		this.addMouseListener(this);
		this.getDocument().addDocumentListener(this);
		this.setFocusable(false);
	}
	
	public void setGrayColor(String text) {
		this.text = text;
		this.setForeground(fontColor);
	}

	public void focusGained(FocusEvent arg0) {
		this.setForeground(fontColor2);
		if(String.valueOf(getPassword()).equals(text))
			this.setText("");
	}

	public void focusLost(FocusEvent arg0) {
		if(String.valueOf(getPassword()).equals("")) {
			this.setText(text);
			this.setForeground(fontColor);
		}
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		if(String.valueOf(getPassword()).equals(text) ||
				String.valueOf(getPassword()).equals(""))
			setBackground(CoinColor.LIGHT_GRAY);
		else
			setBackground(CoinColor.WHITE);
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		if(String.valueOf(getPassword()).equals(text) ||
				String.valueOf(getPassword()).equals(""))
			setBackground(CoinColor.LIGHT_GRAY);
		else
			setBackground(CoinColor.WHITE);
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
	}
	
	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
		this.setFocusable(true);
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}
}