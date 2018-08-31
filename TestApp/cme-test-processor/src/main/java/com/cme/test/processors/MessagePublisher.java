package com.cme.test.processors;

import com.cme.test.events.MessagePublish;

public class MessagePublisher {

	public void publishMessage(MessagePublish messagePublish) {
		System.out.println("Message:- " + messagePublish.getMessage()
				+ ", partition:- " + messagePublish.getPartition());
	}
}
