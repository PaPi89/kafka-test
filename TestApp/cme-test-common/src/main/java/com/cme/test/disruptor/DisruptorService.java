package com.cme.test.disruptor;

import com.cme.test.config.beans.DisruptorConfig;

public interface DisruptorService {

	void init();
	
	void start();

	void process(DisruptorConfig disruptorConfig);
	
	void stop();
	
	void reset();
}
