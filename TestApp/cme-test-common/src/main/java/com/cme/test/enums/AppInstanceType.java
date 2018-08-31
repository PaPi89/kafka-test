package com.cme.test.enums;

public enum AppInstanceType {

	CONSUMER("consumer"),
	PRODUCER("producer");
	
	private String value;
	
	private AppInstanceType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
