package com.cme.test.processors;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.cme.test.beans.ConfigDetail;
import com.cme.test.constants.AppConstants;
import com.cme.test.enums.TransportType;
import com.cme.test.events.MessagePublish;
import com.cme.test.factories.ProducerFactory;
import com.cme.test.producers.Producer;
import com.lmax.disruptor.EventHandler;

public class MessagePublisher implements EventHandler<MessagePublish> {
	
	private Producer targetProducer;
	
	public MessagePublisher() {
	}
	
	public MessagePublisher(ConfigDetail configurations) {
		List<Integer> partitions = Arrays
				.asList(AppConstants.DEFAULT_TOPIC_PARTITIONS);

		if ((configurations.getCommonProperties()
				.getProperty(AppConstants.TARGET_TOPIC_PARTITIONS) != null)
				&& (!configurations.getCommonProperties()
						.getProperty(AppConstants.TARGET_TOPIC_PARTITIONS)
						.isEmpty())) {
			partitions = Arrays
					.asList(configurations.getCommonProperties()
							.getProperty(AppConstants.TARGET_TOPIC_PARTITIONS)
							.split(","))
					.stream().map(m -> Integer.parseInt(m))
					.collect(Collectors.toList());
		}

		this.targetProducer = new ProducerFactory().getProducer(
				TransportType.KAFKA.getValue(), configurations,
				AppConstants.TARGET_TOPIC, partitions);
	}
	
	@Override
	public void onEvent(MessagePublish event, long sequence, boolean endOfBatch)
			throws Exception {
		
		System.out.println("Event:- " + event.getMessage() + ", partition:- "
				+ event.getPartition() + ", Sequence:- " + sequence
				+ ", End of Batch:- " + endOfBatch);
		
		targetProducer.sendMessage(event.getMessage());
		
	}

}
