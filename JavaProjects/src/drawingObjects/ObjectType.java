package drawingObjects;

public enum ObjectType {
	LINE(0),
	ICON(1),
	OVAL(2),
	RECTANGLE(3),
	TAG(4),
	SELECT(5);

	private int value;
	
	private ObjectType() {}
	
	private ObjectType(int v) {
		value = v;
	}
	
	public int getInt() {
		return value;
	}
}