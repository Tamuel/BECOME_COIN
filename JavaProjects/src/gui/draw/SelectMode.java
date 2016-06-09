package gui.draw;

public enum SelectMode {
	NONE(0),
	MOVE(1),
	LINE_BEGIN(2),
	LINE_END(3),
	UPPER_SIDE(4),
	LOWER_SIDE(5),
	LEFT_SIDE(6),
	RIGHT_SIDE(7);

	private int value;
	
	private SelectMode() {}
	
	private SelectMode(int value) {
		this.value = value;
	}
	
	public int getInt() {
		return this.value;
	}
}
