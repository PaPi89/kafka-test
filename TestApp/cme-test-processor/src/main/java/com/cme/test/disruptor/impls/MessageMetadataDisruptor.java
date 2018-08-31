package com.cme.test.disruptor.impls;

import com.cme.test.config.beans.DisruptorConfig;
import com.cme.test.constants.DisruptorConstant;
import com.cme.test.disruptor.DisruptorService;
import com.cme.test.events.MessageMetadata;
import com.cme.test.processors.MessageProcessor;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class MessageMetadataDisruptor implements DisruptorService {

	private Disruptor<MessageMetadata> disruptor;

	private RingBuffer<MessageMetadata> ringBuffer;

	private final MessageProcessor messageProcessor = new MessageProcessor(
			new MessagePublisherDisruptor());

	@Override
	public void init() {
		disruptor = new Disruptor<>(MessageMetadata::new,
				DisruptorConstant.RING_BUFFER_SIZE,
				DaemonThreadFactory.INSTANCE);
		disruptor.handleEventsWith((MessageMetadata event, long sequence,
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
		MessageMetadata metadata = ringBuffer.get(seq);
		metadata.setMessage(disruptorConfig.getMessage());
		metadata.setPartition(disruptorConfig.getPartition());
		ringBuffer.publish(seq);
	}

	@Override
	public void stop() {
		disruptor.shutdown();

	}

	@Override
	public void reset() {
	}

	private void handleEvent(MessageMetadata event, long sequence,
			boolean endOfBatch) {

		messageProcessor.processMessage(event);

	}

}
