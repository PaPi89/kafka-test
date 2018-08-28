package com.cme.test.enums;

public enum TransportType {

	FTL("FTL"),
	KAFKA("KAFKA");
	
	private String value;
	
	private TransportType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
