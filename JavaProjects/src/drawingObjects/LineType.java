package drawingObjects;

public enum LineType {
	NORMAL(0),
	DOTTED(1),
	DOUBLE(2);

	private int value;
	
	private LineType() {}
	
	private LineType(int v) {
		value = v;
	}
	
	public int getInt() {
		return value;
	}
}
