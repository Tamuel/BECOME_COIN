package gui.draw;

import javax.swing.JPanel;

import resource.CoinColor;

public class CoinCanvasPanel extends JPanel{

	private CoinToolMode toolMode;

	public CoinCanvasPanel() {
		this.setBackground(CoinColor.WHITE);
		this.setLayout(null);
	}
	
	public CoinToolMode getToolMode() {
		return toolMode;
	}

	public void setToolMode(CoinToolMode toolMode) {
		this.toolMode = toolMode;
	}
}
