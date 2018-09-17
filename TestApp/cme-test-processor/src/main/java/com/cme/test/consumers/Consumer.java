package com.cme.test.consumers;

public interface Consumer {
	
	void init();

	void start();
	
	void close();
	
	void receiveMessages();
	
}
