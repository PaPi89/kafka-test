package com.cme.test.processors;

import com.cme.test.config.beans.DisruptorConfig;
import com.cme.test.disruptor.DisruptorService;
import com.cme.test.events.MessageMetadata;

public class MessageProcessor {
	
	private DisruptorService disruptorService;
	
	public MessageProcessor(DisruptorService disruptorService) {
		this.disruptorService = disruptorService;
		this.disruptorService.init();
		this.disruptorService.start();
	}

	public void processMessage(MessageMetadata messageMetadata) {
		System.out.println("Message:- " + messageMetadata.getMessage()
				+ ", partition:- " + messageMetadata.getPartition());
		
		DisruptorConfig disruptorConfig = new DisruptorConfig();
		disruptorConfig.setMessage(messageMetadata.getMessage() + "-target");
		disruptorConfig.setPartition(messageMetadata.getPartition());
		
		disruptorService.process(disruptorConfig);
	}
}
