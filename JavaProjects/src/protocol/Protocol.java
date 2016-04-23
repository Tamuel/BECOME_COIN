package protocol;

public class Protocol {

	private ProtocolType protocolType;
	private Object data;
	
	public Protocol(ProtocolType protocolType, Object data) {
		this.protocolType = protocolType;
		this.data = data;
	}
	
	public ProtocolType getType() {
		return protocolType;
	}

	public void setType(ProtocolType protocolType) {
		this.protocolType = protocolType;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
