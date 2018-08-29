package com.cme.test.processors;

import com.cme.test.beans.ConfigDetail;
import com.cme.test.constants.AppConstants;
import com.cme.test.disruptors.DisruptorFactory;
import com.cme.test.events.MessageMetadata;
import com.cme.test.events.MessagePublish;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class MessageProcessor implements EventHandler<MessageMetadata> {

	private RingBuffer<MessagePublish> targetRingBuffer;

	public MessageProcessor() {
	}

	public MessageProcessor(ConfigDetail configurations) {
		
		DisruptorFactory<MessagePublish> disruptorFactory = new DisruptorFactory<>();
		Disruptor<MessagePublish> targetDisruptor = disruptorFactory
				.getDisruptor(MessagePublish.class,
						AppConstants.RING_BUFFER_SIZE,
						DaemonThreadFactory.INSTANCE);
		targetDisruptor.handleEventsWith(new MessagePublisher(configurations));
		this.targetRingBuffer = targetDisruptor.start();
		
	}

	@Override
	public void onEvent(MessageMetadata event, long sequence,
			boolean endOfBatch) throws Exception {

		System.out.println("Event:- " + event.getMessage() + ", partition:- "
				+ event.getPartition() + ", Sequence:- " + sequence
				+ ", End of Batch:- " + endOfBatch);

		long targetSeq = targetRingBuffer.next();
		MessagePublish msgPublishEvent = targetRingBuffer.get(targetSeq);
		msgPublishEvent.setMessage(event.getMessage());
		msgPublishEvent.setPartition(event.getPartition());
		targetRingBuffer.publish(targetSeq);
	}

}
