package com.cme.test.processors;

import com.cme.test.events.MessageMetadata;
import com.lmax.disruptor.EventHandler;

public class MessageProcessor implements EventHandler<MessageMetadata> {

	// private RingBuffer<MessageMetadata> ringBuffer;

	/*public MessageProcessor() {
	}*/

	/*
	 * public MessageProcessor(RingBuffer<MessageMetadata> ringBuffer) {
	 * this.ringBuffer = ringBuffer; }
	 */

	@Override
	public void onEvent(MessageMetadata event, long sequence,
			boolean endOfBatch) throws Exception {

		System.out.println("Event:- " + event.getMessage() + ", Sequence:- "
				+ sequence + ", End of Batch:- " + endOfBatch);
	}

}
