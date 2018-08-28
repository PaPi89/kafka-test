package com.cme.test.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.cme.test.beans.ConfigDetail;

public class KfkProducer implements Producer {

	private ConfigDetail configurations;

	private KafkaProducer<String, String> producer;

	private String topic;

	public KfkProducer() {
	}

	public KfkProducer(ConfigDetail configDetails, String topic) {
		this.configurations = configDetails;
		this.producer = new KafkaProducer<>(
				configDetails.getProducerProperties());
		this.topic = topic;
	}
	
	@Override
	public void executeProducer() {
		sendMessage("message");
	}

	public void sendMessage(String message) {

		try {
			producer.send(new ProducerRecord<String, String>(topic, message));

			producer.flush();
		}
		catch (Throwable throwable) {
			throwable.printStackTrace();
		}

	}

}
