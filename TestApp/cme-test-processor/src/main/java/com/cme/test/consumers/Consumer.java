package com.cme.test.consumers;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;

public interface Consumer {
	
	void init();

	void start();
	
	void close();
	
	void receiveMessages();
	
}
