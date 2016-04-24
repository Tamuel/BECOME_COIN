package drawingObjects;

public enum VertexType {
	START(0),
	END(0);

	private int value;
	
	private VertexType() {}
	
	private VertexType(int v) {
		value = v;
	}
	
	public int getInt() {
		return value;
	}
}