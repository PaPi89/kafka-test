package com.cme.test.producers;

public interface Producer {

	void executeProducer();
	
	void sendMessage(String message);
}
