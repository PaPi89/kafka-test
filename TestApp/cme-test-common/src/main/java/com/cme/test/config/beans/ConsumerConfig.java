package com.cme.test.config.beans;

import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;

import com.cme.test.disruptor.DisruptorService;

public class ConsumerConfig {

	private Properties kafkaConsumerProperties;
	
	private List<String> kafkaTopics;
	
	private ConsumerRebalanceListener kafkaConsumerRebalanceListener;
	
	private DisruptorService disruptorService;

	public Properties getKafkaConsumerProperties() {
		return kafkaConsumerProperties;
	}

	public void setKafkaConsumerProperties(Properties kafkaConsumerProperties) {
		this.kafkaConsumerProperties = kafkaConsumerProperties;
	}

	public List<String> getKafkaTopics() {
		return kafkaTopics;
	}

	public void setKafkaTopics(List<String> kafkaTopics) {
		this.kafkaTopics = kafkaTopics;
	}

	public ConsumerRebalanceListener getKafkaConsumerRebalanceListener() {
		return kafkaConsumerRebalanceListener;
	}

	public void setKafkaConsumerRebalanceListener(
			ConsumerRebalanceListener kafkaConsumerRebalanceListener) {
		this.kafkaConsumerRebalanceListener = kafkaConsumerRebalanceListener;
	}

	public DisruptorService getDisruptorService() {
		return disruptorService;
	}

	public void setDisruptorService(DisruptorService disruptorService) {
		this.disruptorService = disruptorService;
	}

}
