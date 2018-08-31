package com.cme.test.disruptor.impls;

import com.cme.test.config.beans.DisruptorConfig;
import com.cme.test.constants.DisruptorConstant;
import com.cme.test.disruptor.DisruptorService;
import com.cme.test.events.MessagePublish;
import com.cme.test.processors.MessagePublisher;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class MessagePublisherDisruptor implements DisruptorService {

	private Disruptor<MessagePublish> disruptor;

	private RingBuffer<MessagePublish> ringBuffer;

	private final MessagePublisher messagePublisher = new MessagePublisher();

	@Override
	public void init() {
		disruptor = new Disruptor<>(MessagePublish::new,
				DisruptorConstant.RING_BUFFER_SIZE,
				DaemonThreadFactory.INSTANCE);
		disruptor.handleEventsWith((MessagePublish event, long sequence,
				boolean endOfBatch) -> handleEvent(event, sequence,
						endOfBatch));
	}

	@Override
	public void start() {
		ringBuffer = disruptor.start();
	}

	@Override
	public void process(DisruptorConfig disruptorConfig) {
		long seq = ringBuffer.next();
		MessagePublish messagePublish = ringBuffer.get(seq);
		messagePublish.setMessage(disruptorConfig.getMessage());
		messagePublish.setPartition(disruptorConfig.getPartition());
		ringBuffer.publish(seq);
	}

	@Override
	public void stop() {
		disruptor.shutdown();
	}

	@Override
	public void reset() {
	}

	private void handleEvent(MessagePublish event, long sequence,
			boolean endOfBatch) {

		messagePublisher.publishMessage(event);

	}
}
