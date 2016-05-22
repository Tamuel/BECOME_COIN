package gui.draw;

public enum CoinToolMode {
	SELECT(0),
	LINE(1),
	RECT(2),
	CIRCLE(3),
	ICON(4),
	TAG(5),
	BEACON(6);

	private int value;
	
	private CoinToolMode() {}
	
	private CoinToolMode(int value) {
		this.value = value;
	}
	
	public int getInt() {
		return this.value;
	}
}
