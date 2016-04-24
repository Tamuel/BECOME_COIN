package drawingObjects;

public enum IconType {
	DOOR(0),
	ELEVATOR(1),
	STAIRS(2),
	TOILET(3),
	ESCALATOR_UP(4),
	ESCALATOR_DOWN(5),
	ESCALATOR_UP_DOWN(6);

	private int value;
	
	private IconType() {}
	
	private IconType(int v) {
		value = v;
	}
	
	public int getInt() {
		return value;
	}
}