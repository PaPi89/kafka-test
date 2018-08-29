package com.cme.test.producers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.cme.test.beans.ConfigDetail;
import com.cme.test.constants.AppConstants;

public class KfkProducer implements Producer {

	private ConfigDetail configurations;

	private KafkaProducer<String, String> producer;

	private String topic;
	
	private List<Integer> partitions;

	public KfkProducer() {
	}

	public KfkProducer(ConfigDetail configDetails, String topic, List<Integer> partitions) {
		this.configurations = configDetails;
		this.producer = new KafkaProducer<>(
				configDetails.getProducerProperties());
		this.topic = topic;
		this.partitions = partitions;
	}

	@Override
	public void executeProducer() {
		for(int i = 0; i < 100; i++) {
			
			sendMessage("source-message" + i);
			
		}
	}

	@Override
	public void sendMessage(String message) {

		try {

			partitions.forEach(partition -> {

				producer.send(new ProducerRecord<String, String>(topic,
						partition, "", message));

				producer.flush();

			});

		}
		catch (Throwable throwable) {
			throwable.printStackTrace();
		}

	}

}
