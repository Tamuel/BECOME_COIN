package protocol;

public enum ProtocolType {
	
	REQUEST_LOGIN(0),
	RESPOND_LOGIN(1);
	
	private int value;
	
	private ProtocolType() {
		
	}
	
	private ProtocolType(int value) {
		this.value = value;
	}
	
	public int getInt() {
		
		return value;
	}
}
